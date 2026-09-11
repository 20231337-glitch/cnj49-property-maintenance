package com.cnj49.propertymaintenance.service;

import com.cnj49.propertymaintenance.dto.ChartSeries;
import com.cnj49.propertymaintenance.entity.MaintenanceRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Bay bao cao theo dac ta muc 31.
 * Moi bao cao deu nhan bo loc fromDate / toDate / property / contractor / category.
 */
public interface ReportService {

    /** Bao cao 1: chi phi van hanh theo thang. */
    ChartSeries expenseByMonth(LocalDate fromDate, LocalDate toDate);

    /** Bao cao 2: chi phi tung bat dong san. */
    ChartSeries expenseByProperty(LocalDate fromDate, LocalDate toDate, Long propertyId);

    /** Bao cao 3: chi phi theo loai. */
    ChartSeries expenseByType(LocalDate fromDate, LocalDate toDate, Long propertyId);

    /** Bao cao 4: so luong su co theo hang muc. */
    ChartSeries requestsByCategory(LocalDate fromDate, LocalDate toDate);

    /** Bao cao 5: nha thau theo tong gia tri cong viec (giam dan). */
    List<Object[]> topContractors(LocalDate fromDate, LocalDate toDate, Long contractorId);

    /** Bao cao 6: yeu cau bao tri theo trang thai. */
    ChartSeries requestsByStatus(LocalDate fromDate, LocalDate toDate);

    /** Bao cao 7: cac yeu cau qua han. */
    List<MaintenanceRequest> overdueRequests();

    /** Tong chi phi ung voi bo loc, hien thi tren dau trang bao cao. */
    BigDecimal totalExpense(LocalDate fromDate, LocalDate toDate, Long propertyId);
}
