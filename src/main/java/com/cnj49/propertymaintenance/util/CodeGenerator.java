package com.cnj49.propertymaintenance.util;

import com.cnj49.propertymaintenance.repository.*;
import org.springframework.stereotype.Component;

import java.time.Year;

/**
 * Sinh ma nghiep vu tu dong. Nguoi dung khong bao gio phai tu nhap ma.
 *
 * Dinh dang:
 *   Property           PROP-0001
 *   Contractor         CTR-0001
 *   MaintenanceRequest MR-2026-0001
 *   Quotation          QT-2026-0001
 *   WorkOrder          WO-2026-0001
 *   Expense            EXP-2026-0001
 */
@Component
public class CodeGenerator {

    private final PropertyRepository propertyRepository;
    private final ContractorRepository contractorRepository;
    private final MaintenanceRequestRepository maintenanceRequestRepository;
    private final QuotationRepository quotationRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ExpenseRepository expenseRepository;

    public CodeGenerator(PropertyRepository propertyRepository,
                         ContractorRepository contractorRepository,
                         MaintenanceRequestRepository maintenanceRequestRepository,
                         QuotationRepository quotationRepository,
                         WorkOrderRepository workOrderRepository,
                         ExpenseRepository expenseRepository) {
        this.propertyRepository = propertyRepository;
        this.contractorRepository = contractorRepository;
        this.maintenanceRequestRepository = maintenanceRequestRepository;
        this.quotationRepository = quotationRepository;
        this.workOrderRepository = workOrderRepository;
        this.expenseRepository = expenseRepository;
    }

    private String currentYear() {
        return String.valueOf(Year.now().getValue());
    }

    private String format(String prefix, Integer maxSequence) {
        int next = (maxSequence == null ? 0 : maxSequence) + 1;
        return prefix + String.format("%04d", next);
    }

    public String nextPropertyCode() {
        return format("PROP-", propertyRepository.findMaxCodeSequence());
    }

    public String nextContractorCode() {
        return format("CTR-", contractorRepository.findMaxCodeSequence());
    }

    public String nextMaintenanceRequestCode() {
        String year = currentYear();
        return format("MR-" + year + "-", maintenanceRequestRepository.findMaxCodeSequenceByYear(year));
    }

    public String nextQuotationCode() {
        String year = currentYear();
        return format("QT-" + year + "-", quotationRepository.findMaxCodeSequenceByYear(year));
    }

    public String nextWorkOrderCode() {
        String year = currentYear();
        return format("WO-" + year + "-", workOrderRepository.findMaxCodeSequenceByYear(year));
    }

    public String nextExpenseCode() {
        String year = currentYear();
        return format("EXP-" + year + "-", expenseRepository.findMaxCodeSequenceByYear(year));
    }
}
