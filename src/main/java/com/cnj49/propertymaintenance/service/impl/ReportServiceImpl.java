package com.cnj49.propertymaintenance.service.impl;

import com.cnj49.propertymaintenance.dto.ChartSeries;
import com.cnj49.propertymaintenance.entity.MaintenanceRequest;
import com.cnj49.propertymaintenance.entity.Property;
import com.cnj49.propertymaintenance.enums.ExpenseType;
import com.cnj49.propertymaintenance.enums.RequestStatus;
import com.cnj49.propertymaintenance.repository.ExpenseRepository;
import com.cnj49.propertymaintenance.repository.MaintenanceRequestRepository;
import com.cnj49.propertymaintenance.repository.PropertyRepository;
import com.cnj49.propertymaintenance.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Bay bao cao nghiep vu, tat ca deu ho tro loc theo khoang ngay / bat dong san / nha thau. */
@Service
public class ReportServiceImpl implements ReportService {

    /** So nam du bao cho Bao cao 8 (gia tang theo tung nam). */
    private static final int PROJECTION_YEARS = 5;

    private final ExpenseRepository expenseRepository;
    private final MaintenanceRequestRepository requestRepository;
    private final PropertyRepository propertyRepository;

    public ReportServiceImpl(ExpenseRepository expenseRepository,
                             MaintenanceRequestRepository requestRepository,
                             PropertyRepository propertyRepository) {
        this.expenseRepository = expenseRepository;
        this.requestRepository = requestRepository;
        this.propertyRepository = propertyRepository;
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

    /** Bao cao 8: du bao gia tung bat dong san sau 5 nam. */
    @Override
    @Transactional(readOnly = true)
    public List<Object[]> propertyPriceProjection() {
        List<Object[]> rows = new ArrayList<>();
        for (Property property : pricedProperties()) {
            BigDecimal basePrice = property.getBasePrice();
            BigDecimal projectedYear5 = projectPrice(basePrice, property.getAnnualIncreaseRate(), PROJECTION_YEARS);
            BigDecimal multiplier = projectedYear5.divide(basePrice, 4, RoundingMode.HALF_UP);
            rows.add(new Object[]{property.getName(), basePrice, projectedYear5, multiplier});
        }
        return rows;
    }

    /** Bao cao 8: tong gia tri danh muc qua tung nam, tu hien tai (nam 0) den nam 5. */
    @Override
    @Transactional(readOnly = true)
    public ChartSeries portfolioValueByYear() {
        List<Property> properties = pricedProperties();
        ChartSeries series = new ChartSeries();
        for (int year = 0; year <= PROJECTION_YEARS; year++) {
            BigDecimal total = BigDecimal.ZERO;
            for (Property property : properties) {
                total = total.add(projectPrice(property.getBasePrice(), property.getAnnualIncreaseRate(), year));
            }
            series.add(year == 0 ? "Hiện tại" : "Năm " + year, total);
        }
        return series;
    }

    /** Chi lay cac property co khai bao gia co ban > 0, dung lam dau vao cho du bao gia. */
    private List<Property> pricedProperties() {
        List<Property> result = new ArrayList<>();
        for (Property property : propertyRepository.findAll()) {
            if (property.getBasePrice() != null && property.getBasePrice().compareTo(BigDecimal.ZERO) > 0) {
                result.add(property);
            }
        }
        return result;
    }

    /** gia(nam) = giaGoc * (1 + tyLe/100) ^ nam. */
    private BigDecimal projectPrice(BigDecimal basePrice, BigDecimal annualRatePercent, int years) {
        BigDecimal rate = annualRatePercent == null ? BigDecimal.ZERO : annualRatePercent;
        BigDecimal factor = BigDecimal.ONE.add(rate.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP));
        return basePrice.multiply(factor.pow(years)).setScale(0, RoundingMode.HALF_UP);
    }
}
