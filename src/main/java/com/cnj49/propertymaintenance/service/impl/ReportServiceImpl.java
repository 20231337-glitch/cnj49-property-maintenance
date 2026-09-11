package com.cnj49.propertymaintenance.service.impl;

import com.cnj49.propertymaintenance.dto.ChartSeries;
import com.cnj49.propertymaintenance.entity.MaintenanceRequest;
import com.cnj49.propertymaintenance.enums.ExpenseType;
import com.cnj49.propertymaintenance.enums.RequestStatus;
import com.cnj49.propertymaintenance.repository.ExpenseRepository;
import com.cnj49.propertymaintenance.repository.MaintenanceRequestRepository;
import com.cnj49.propertymaintenance.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Bay bao cao nghiep vu, tat ca deu ho tro loc theo khoang ngay / bat dong san / nha thau. */
@Service
public class ReportServiceImpl implements ReportService {

    private final ExpenseRepository expenseRepository;
    private final MaintenanceRequestRepository requestRepository;

    public ReportServiceImpl(ExpenseRepository expenseRepository,
                             MaintenanceRequestRepository requestRepository) {
        this.expenseRepository = expenseRepository;
        this.requestRepository = requestRepository;
    }

    /** Bao cao 1: chi phi van hanh theo thang. */
    @Override
    @Transactional(readOnly = true)
    public ChartSeries expenseByMonth(LocalDate fromDate, LocalDate toDate) {
        ChartSeries series = new ChartSeries();
        for (Object[] row : expenseRepository.sumGroupByMonth(fromDate, toDate)) {
            int year = (Integer) row[0];
            int month = (Integer) row[1];
            BigDecimal sum = (BigDecimal) row[2];
            series.add("Th" + month + "/" + year, sum);
        }
        return series;
    }

    /** Bao cao 2: chi phi tung bat dong san. */
    @Override
    @Transactional(readOnly = true)
    public ChartSeries expenseByProperty(LocalDate fromDate, LocalDate toDate, Long propertyId) {
        ChartSeries series = new ChartSeries();
        for (Object[] row : expenseRepository.sumGroupByProperty(fromDate, toDate, propertyId)) {
            series.add((String) row[0], (BigDecimal) row[1]);
        }
        return series;
    }

    /** Bao cao 3: chi phi theo loai. */
    @Override
    @Transactional(readOnly = true)
    public ChartSeries expenseByType(LocalDate fromDate, LocalDate toDate, Long propertyId) {
        ChartSeries series = new ChartSeries();
        for (Object[] row : expenseRepository.sumGroupByType(fromDate, toDate, propertyId)) {
            ExpenseType type = (ExpenseType) row[0];
            series.add(type.getLabel(), (BigDecimal) row[1]);
        }
        return series;
    }

    /** Bao cao 4: so luong su co theo hang muc. */
    @Override
    @Transactional(readOnly = true)
    public ChartSeries requestsByCategory(LocalDate fromDate, LocalDate toDate) {
        ChartSeries series = new ChartSeries();
        for (Object[] row : requestRepository.countGroupByCategoryBetween(fromDate, toDate)) {
            series.add((String) row[0], (Long) row[1]);
        }
        return series;
    }

    /** Bao cao 5: nha thau theo tong gia tri cong viec giam dan. */
    @Override
    @Transactional(readOnly = true)
    public List<Object[]> topContractors(LocalDate fromDate, LocalDate toDate, Long contractorId) {
        return expenseRepository.sumGroupByContractor(fromDate, toDate, contractorId);
    }

    /** Bao cao 6: yeu cau bao tri theo trang thai. */
    @Override
    @Transactional(readOnly = true)
    public ChartSeries requestsByStatus(LocalDate fromDate, LocalDate toDate) {
        ChartSeries series = new ChartSeries();
        for (Object[] row : requestRepository.countGroupByStatusBetween(fromDate, toDate)) {
            RequestStatus status = (RequestStatus) row[0];
            series.add(status.getLabel(), (Long) row[1]);
        }
        return series;
    }

    /** Bao cao 7: cac yeu cau qua han. */
    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceRequest> overdueRequests() {
        return requestRepository.findOverdueRequests(LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal totalExpense(LocalDate fromDate, LocalDate toDate, Long propertyId) {
        return expenseRepository.sumGroupByProperty(fromDate, toDate, propertyId).stream()
                .map(row -> (BigDecimal) row[1])
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
