package com.cnj49.propertymaintenance.service.impl;

import com.cnj49.propertymaintenance.dto.InspectionForm;
import com.cnj49.propertymaintenance.entity.Expense;
import com.cnj49.propertymaintenance.entity.Inspection;
import com.cnj49.propertymaintenance.entity.MaintenanceRequest;
import com.cnj49.propertymaintenance.entity.WorkOrder;
import com.cnj49.propertymaintenance.enums.AuditAction;
import com.cnj49.propertymaintenance.enums.ExpenseType;
import com.cnj49.propertymaintenance.enums.InspectionResult;
import com.cnj49.propertymaintenance.enums.RequestStatus;
import com.cnj49.propertymaintenance.enums.WorkOrderStatus;
import com.cnj49.propertymaintenance.exception.BusinessException;
import com.cnj49.propertymaintenance.exception.ResourceNotFoundException;
import com.cnj49.propertymaintenance.repository.ExpenseRepository;
import com.cnj49.propertymaintenance.repository.InspectionRepository;
import com.cnj49.propertymaintenance.repository.MaintenanceRequestRepository;
import com.cnj49.propertymaintenance.repository.WorkOrderRepository;
import com.cnj49.propertymaintenance.service.AuditLogService;
import com.cnj49.propertymaintenance.service.InspectionService;
import com.cnj49.propertymaintenance.util.CodeGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InspectionServiceImpl implements InspectionService {

    private final InspectionRepository inspectionRepository;
    private final WorkOrderRepository workOrderRepository;
    private final MaintenanceRequestRepository requestRepository;
    private final ExpenseRepository expenseRepository;
    private final CodeGenerator codeGenerator;
    private final AuditLogService auditLogService;

    public InspectionServiceImpl(InspectionRepository inspectionRepository,
                                 WorkOrderRepository workOrderRepository,
                                 MaintenanceRequestRepository requestRepository,
                                 ExpenseRepository expenseRepository,
                                 CodeGenerator codeGenerator,
                                 AuditLogService auditLogService) {
        this.inspectionRepository = inspectionRepository;
        this.workOrderRepository = workOrderRepository;
        this.requestRepository = requestRepository;
        this.expenseRepository = expenseRepository;
        this.codeGenerator = codeGenerator;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Inspection> findAll(Pageable pageable) {
        return inspectionRepository.findAllByOrderByInspectionDateDesc(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Inspection> findByWorkOrder(Long workOrderId) {
        return inspectionRepository.findByWorkOrderIdOrderByInspectionDateDesc(workOrderId);
    }

    @Override
    @Transactional(readOnly = true)
    public Inspection findById(Long id) {
        return inspectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("biên bản nghiệm thu", id));
    }

    /**
     * Nghiem thu mot phieu cong viec.
     *
     * BR04: chi nghiem thu duoc WorkOrder da COMPLETED.
     * BR12 (PASSED): WorkOrder -> ACCEPTED, MaintenanceRequest -> COMPLETED,
     *                dong thoi ghi nhan chi phi thuc te vao bang expenses.
     * BR13 (FAILED): WorkOrder -> IN_PROGRESS, MaintenanceRequest -> IN_PROGRESS (lam lai).
     */
    @Override
    @Transactional
    public Inspection inspect(InspectionForm form) {
        WorkOrder workOrder = workOrderRepository.findById(form.getWorkOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("phiếu công việc", form.getWorkOrderId()));

        // BR04
        if (workOrder.getStatus() != WorkOrderStatus.COMPLETED) {
            throw new BusinessException(
                    "Không thể nghiệm thu phiếu công việc chưa hoàn thành (BR04). Trạng thái hiện tại: "
                            + workOrder.getStatus().getLabel());
        }

        // BR07
        if (form.getActualCost() == null || form.getActualCost().signum() < 0) {
            throw new BusinessException("Chi phí thực tế không được âm (BR07).");
        }

        // BR06: ngay nghiem thu khong duoc truoc ngay bat dau cong viec.
        if (form.getInspectionDate().isBefore(workOrder.getStartDate())) {
            throw new BusinessException("Ngày nghiệm thu không được trước ngày bắt đầu công việc (BR06).");
        }

        Inspection inspection = new Inspection();
        inspection.setWorkOrder(workOrder);
        inspection.setInspectionDate(form.getInspectionDate());
        inspection.setInspectorName(form.getInspectorName());
        inspection.setResult(form.getResult());
        inspection.setActualCost(form.getActualCost());
        inspection.setNotes(form.getNotes());
        Inspection saved = inspectionRepository.save(inspection);

        MaintenanceRequest request = workOrder.getMaintenanceRequest();

        if (form.getResult() == InspectionResult.PASSED) {
            // BR12
            workOrder.setStatus(WorkOrderStatus.ACCEPTED);
            workOrderRepository.save(workOrder);

            request.setStatus(RequestStatus.COMPLETED);
            request.setActualCompletionDate(form.getInspectionDate());
            requestRepository.save(request);

            recordActualExpense(workOrder, saved);

            auditLogService.log(AuditAction.INSPECTION_PASSED, "MaintenanceRequest", request.getId(),
                    "Nghiệm thu ĐẠT phiếu " + workOrder.getWorkOrderCode()
                            + ", chi phí thực tế " + saved.getActualCost().toPlainString());
        } else {
            // BR13
            workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
            workOrder.setActualCompletionDate(null);
            workOrderRepository.save(workOrder);

            request.setStatus(RequestStatus.IN_PROGRESS);
            requestRepository.save(request);

            auditLogService.log(AuditAction.INSPECTION_FAILED, "MaintenanceRequest", request.getId(),
                    "Nghiệm thu KHÔNG ĐẠT phiếu " + workOrder.getWorkOrderCode() + ", yêu cầu làm lại");
        }

        return saved;
    }

    /**
     * Khi nghiem thu dat, he thong tu dong ghi nhan chi phi thuc te
     * de bao cao va dashboard phan anh dung so tien da chi.
     */
    private void recordActualExpense(WorkOrder workOrder, Inspection inspection) {
        if (inspection.getActualCost().signum() == 0) {
            return;
        }
        Expense expense = new Expense();
        expense.setExpenseCode(codeGenerator.nextExpenseCode());
        expense.setProperty(workOrder.getMaintenanceRequest().getProperty());
        expense.setWorkOrder(workOrder);
        expense.setContractor(workOrder.getContractor());
        expense.setExpenseType(ExpenseType.MAINTENANCE);
        expense.setAmount(inspection.getActualCost());
        expense.setExpenseDate(inspection.getInspectionDate());
        expense.setDescription("Chi phí bảo trì theo phiếu " + workOrder.getWorkOrderCode()
                + " - " + workOrder.getMaintenanceRequest().getTitle());
        expense.setReferenceNumber(workOrder.getWorkOrderCode());
        expenseRepository.save(expense);

        auditLogService.log(AuditAction.CREATE_EXPENSE, "MaintenanceRequest",
                workOrder.getMaintenanceRequest().getId(),
                "Ghi nhận chi phí " + expense.getExpenseCode()
                        + " - " + expense.getAmount().toPlainString());
    }
}
