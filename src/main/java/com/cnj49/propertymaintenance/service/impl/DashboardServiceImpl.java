package com.cnj49.propertymaintenance.service.impl;

import com.cnj49.propertymaintenance.dto.ChartSeries;
import com.cnj49.propertymaintenance.dto.DashboardStats;
import com.cnj49.propertymaintenance.enums.RequestStatus;
import com.cnj49.propertymaintenance.repository.*;
import com.cnj49.propertymaintenance.service.DashboardService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.Map;

/** Tong hop so lieu cho Dashboard, tat ca truy van truc tiep tu database. */
@Service
public class DashboardServiceImpl implements DashboardService {

    private final PropertyRepository propertyRepository;
    private final UnitRepository unitRepository;
    private final MaintenanceRequestRepository requestRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ExpenseRepository expenseRepository;

    public DashboardServiceImpl(PropertyRepository propertyRepository,
                                UnitRepository unitRepository,
                                MaintenanceRequestRepository requestRepository,
                                WorkOrderRepository workOrderRepository,
                                ExpenseRepository expenseRepository) {
        this.propertyRepository = propertyRepository;
        this.unitRepository = unitRepository;
        this.requestRepository = requestRepository;
        this.workOrderRepository = workOrderRepository;
        this.expenseRepository = expenseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStats getStats() {
        DashboardStats stats = new DashboardStats();
        stats.setTotalProperties(propertyRepository.count());
        stats.setTotalUnits(unitRepository.count());
        stats.setOpenRequests(requestRepository.countOpenRequests());
        stats.setUrgentRequests(requestRepository.countUrgentOpenRequests());
        stats.setActiveWorkOrders(workOrderRepository.countActiveWorkOrders());

        YearMonth now = YearMonth.now();
        stats.setCurrentMonthExpense(expenseRepository.sumBetween(now.atDay(1), now.atEndOfMonth()));
        return stats;
    }

    /** Chart 1: chi phi N thang gan nhat, kem cac thang khong phat sinh chi phi (gia tri 0). */
    @Override
    @Transactional(readOnly = true)
    public ChartSeries expensesByMonth(int months) {
        YearMonth end = YearMonth.now();
        YearMonth start = end.minusMonths(months - 1L);

        Map<YearMonth, BigDecimal> byMonth = new LinkedHashMap<>();
        for (YearMonth ym = start; !ym.isAfter(end); ym = ym.plusMonths(1)) {
            byMonth.put(ym, BigDecimal.ZERO);
        }

        for (Object[] row : expenseRepository.sumGroupByMonth(start.atDay(1), end.atEndOfMonth())) {
            int year = (Integer) row[0];
            int month = (Integer) row[1];
            BigDecimal sum = (BigDecimal) row[2];
            byMonth.put(YearMonth.of(year, month), sum);
        }

        ChartSeries series = new ChartSeries();
        byMonth.forEach((ym, sum) -> series.add(
                "Th" + ym.getMonthValue() + "/" + ym.getYear(), sum));
        return series;
    }

    /** Chart 2: so yeu cau theo trang thai, dung nhan tieng Viet cua enum. */
    @Override
    @Transactional(readOnly = true)
    public ChartSeries requestsByStatus() {
        ChartSeries series = new ChartSeries();
        for (Object[] row : requestRepository.countGroupByStatus()) {
            RequestStatus status = (RequestStatus) row[0];
            Long count = (Long) row[1];
            series.add(status.getLabel(), count);
        }
        return series;
    }

    /** Chart 3: chi phi theo bat dong san. */
    @Override
    @Transactional(readOnly = true)
    public ChartSeries expensesByProperty() {
        ChartSeries series = new ChartSeries();
        for (Object[] row : expenseRepository.sumGroupByProperty(null, null, null)) {
            series.add((String) row[0], (BigDecimal) row[1]);
        }
        return series;
    }

    /** Chart 4: so su co theo hang muc. */
    @Override
    @Transactional(readOnly = true)
    public ChartSeries requestsByCategory() {
        ChartSeries series = new ChartSeries();
        for (Object[] row : requestRepository.countGroupByCategory()) {
            series.add((String) row[0], (Long) row[1]);
        }
        return series;
    }
}
