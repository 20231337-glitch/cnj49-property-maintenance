package com.cnj49.propertymaintenance.service.impl;

import com.cnj49.propertymaintenance.dto.ExpenseForm;
import com.cnj49.propertymaintenance.dto.ExpenseSummary;
import com.cnj49.propertymaintenance.entity.Contractor;
import com.cnj49.propertymaintenance.entity.Expense;
import com.cnj49.propertymaintenance.entity.Property;
import com.cnj49.propertymaintenance.entity.WorkOrder;
import com.cnj49.propertymaintenance.enums.AuditAction;
import com.cnj49.propertymaintenance.enums.ExpenseType;
import com.cnj49.propertymaintenance.exception.BusinessException;
import com.cnj49.propertymaintenance.exception.ResourceNotFoundException;
import com.cnj49.propertymaintenance.repository.ContractorRepository;
import com.cnj49.propertymaintenance.repository.ExpenseRepository;
import com.cnj49.propertymaintenance.repository.PropertyRepository;
import com.cnj49.propertymaintenance.repository.WorkOrderRepository;
import com.cnj49.propertymaintenance.service.AuditLogService;
import com.cnj49.propertymaintenance.service.ExpenseService;
import com.cnj49.propertymaintenance.util.CodeGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final PropertyRepository propertyRepository;
    private final ContractorRepository contractorRepository;
    private final WorkOrderRepository workOrderRepository;
    private final CodeGenerator codeGenerator;
    private final AuditLogService auditLogService;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository,
                              PropertyRepository propertyRepository,
                              ContractorRepository contractorRepository,
                              WorkOrderRepository workOrderRepository,
                              CodeGenerator codeGenerator,
                              AuditLogService auditLogService) {
        this.expenseRepository = expenseRepository;
        this.propertyRepository = propertyRepository;
        this.contractorRepository = contractorRepository;
        this.workOrderRepository = workOrderRepository;
        this.codeGenerator = codeGenerator;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Expense> search(String keyword, Long propertyId, Long contractorId, ExpenseType type,
                                LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        return expenseRepository.search(normalize(keyword), propertyId, contractorId, type,
                fromDate, toDate, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseSummary summarize(String keyword, Long propertyId, Long contractorId, ExpenseType type,
                                    LocalDate fromDate, LocalDate toDate) {
        String normalized = normalize(keyword);
        ExpenseSummary summary = new ExpenseSummary();
        summary.setTotal(expenseRepository.sumBySearch(normalized, propertyId, contractorId, type, fromDate, toDate));
        summary.setMaintenance(expenseRepository.sumBySearch(normalized, propertyId, contractorId,
                ExpenseType.MAINTENANCE, fromDate, toDate));
        summary.setLabor(expenseRepository.sumBySearch(normalized, propertyId, contractorId,
                ExpenseType.LABOR, fromDate, toDate));
        summary.setMaterial(expenseRepository.sumBySearch(normalized, propertyId, contractorId,
                ExpenseType.MATERIAL, fromDate, toDate));

        BigDecimal other = summary.getTotal()
                .subtract(summary.getMaintenance())
                .subtract(summary.getLabor())
                .subtract(summary.getMaterial());
        summary.setOther(other.signum() < 0 ? BigDecimal.ZERO : other);
        return summary;
    }

    @Override
    @Transactional(readOnly = true)
    public Expense findById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("chi phí", id));
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseForm toForm(Expense expense) {
        ExpenseForm form = new ExpenseForm();
        form.setId(expense.getId());
        form.setExpenseCode(expense.getExpenseCode());
        form.setPropertyId(expense.getProperty().getId());
        form.setWorkOrderId(expense.getWorkOrder() != null ? expense.getWorkOrder().getId() : null);
        form.setContractorId(expense.getContractor() != null ? expense.getContractor().getId() : null);
        form.setExpenseType(expense.getExpenseType());
        form.setAmount(expense.getAmount());
        form.setExpenseDate(expense.getExpenseDate());
        form.setDescription(expense.getDescription());
        form.setReferenceNumber(expense.getReferenceNumber());
        form.setNotes(expense.getNotes());
        return form;
    }

    @Override
    @Transactional
    public Expense create(ExpenseForm form) {
        Expense expense = new Expense();
        applyForm(expense, form);
        expense.setExpenseCode(codeGenerator.nextExpenseCode());
        Expense saved = expenseRepository.save(expense);
        auditLogService.log(AuditAction.CREATE_EXPENSE, "Expense", saved.getId(),
                "Ghi nhận chi phí " + saved.getExpenseCode() + " - " + saved.getAmount().toPlainString());
        return saved;
    }

    @Override
    @Transactional
    public Expense update(Long id, ExpenseForm form) {
        Expense expense = findById(id);
        applyForm(expense, form);
        return expenseRepository.save(expense);
    }

    /** Gan du lieu form, kiem tra BR07 (so tien khong am) va cac quan he tuy chon. */
    private void applyForm(Expense expense, ExpenseForm form) {
        // BR07
        if (form.getAmount() == null || form.getAmount().signum() < 0) {
            throw new BusinessException("Số tiền chi phí không được âm (BR07).");
        }

        Property property = propertyRepository.findById(form.getPropertyId())
                .orElseThrow(() -> new BusinessException("Bất động sản không tồn tại"));

        WorkOrder workOrder = null;
        if (form.getWorkOrderId() != null) {
            workOrder = workOrderRepository.findById(form.getWorkOrderId())
                    .orElseThrow(() -> new BusinessException("Phiếu công việc không tồn tại"));
        }

        Contractor contractor = null;
        if (form.getContractorId() != null) {
            contractor = contractorRepository.findById(form.getContractorId())
                    .orElseThrow(() -> new BusinessException("Nhà thầu không tồn tại"));
        }

        expense.setProperty(property);
        expense.setWorkOrder(workOrder);
        expense.setContractor(contractor);
        expense.setExpenseType(form.getExpenseType());
        expense.setAmount(form.getAmount());
        expense.setExpenseDate(form.getExpenseDate());
        expense.setDescription(form.getDescription());
        expense.setReferenceNumber(form.getReferenceNumber());
        expense.setNotes(form.getNotes());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        expenseRepository.delete(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal sumByProperty(Long propertyId) {
        return expenseRepository.sumByPropertyId(propertyId);
    }

    /** Card Dashboard: tong chi phi cua thang hien tai. */
    @Override
    @Transactional(readOnly = true)
    public BigDecimal sumCurrentMonth() {
        YearMonth now = YearMonth.now();
        return expenseRepository.sumBetween(now.atDay(1), now.atEndOfMonth());
    }

    private String normalize(String keyword) {
        return StringUtils.hasText(keyword) ? keyword.trim() : null;
    }
}
