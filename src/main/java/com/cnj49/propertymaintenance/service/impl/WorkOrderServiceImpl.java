package com.cnj49.propertymaintenance.service.impl;

import com.cnj49.propertymaintenance.dto.WorkOrderForm;
import com.cnj49.propertymaintenance.entity.MaintenanceRequest;
import com.cnj49.propertymaintenance.entity.Quotation;
import com.cnj49.propertymaintenance.entity.WorkOrder;
import com.cnj49.propertymaintenance.enums.AuditAction;
import com.cnj49.propertymaintenance.enums.QuotationStatus;
import com.cnj49.propertymaintenance.enums.RequestStatus;
import com.cnj49.propertymaintenance.enums.WorkOrderStatus;
import com.cnj49.propertymaintenance.exception.BusinessException;
import com.cnj49.propertymaintenance.exception.ResourceNotFoundException;
import com.cnj49.propertymaintenance.repository.MaintenanceRequestRepository;
import com.cnj49.propertymaintenance.repository.QuotationRepository;
import com.cnj49.propertymaintenance.repository.WorkOrderRepository;
import com.cnj49.propertymaintenance.service.AuditLogService;
import com.cnj49.propertymaintenance.service.WorkOrderService;
import com.cnj49.propertymaintenance.util.CodeGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final QuotationRepository quotationRepository;
    private final MaintenanceRequestRepository requestRepository;
    private final CodeGenerator codeGenerator;
    private final AuditLogService auditLogService;

    public WorkOrderServiceImpl(WorkOrderRepository workOrderRepository,
                                QuotationRepository quotationRepository,
                                MaintenanceRequestRepository requestRepository,
                                CodeGenerator codeGenerator,
                                AuditLogService auditLogService) {
        this.workOrderRepository = workOrderRepository;
        this.quotationRepository = quotationRepository;
        this.requestRepository = requestRepository;
        this.codeGenerator = codeGenerator;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkOrder> search(String keyword, Long contractorId, WorkOrderStatus status, Pageable pageable) {
        String normalized = StringUtils.hasText(keyword) ? keyword.trim() : null;
        return workOrderRepository.search(normalized, contractorId, status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkOrder findById(Long id) {
        return workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("phiếu công việc", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> findByRequest(Long requestId) {
        return workOrderRepository.findByMaintenanceRequestIdOrderByCreatedAtDesc(requestId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> findByContractor(Long contractorId) {
        return workOrderRepository.findByContractorIdOrderByStartDateDesc(contractorId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> findAwaitingInspection() {
        return workOrderRepository.findByStatusOrderByActualCompletionDateDesc(WorkOrderStatus.COMPLETED);
    }

    /**
     * BR01: chi tao phieu cong viec khi yeu cau da co bao gia APPROVED.
     * BR09: nha thau cua phieu cong viec lay tu bao gia da duyet, khong cho chon tay.
     * BR06: ngay du kien hoan thanh khong duoc truoc ngay bat dau.
     */
    @Override
    @Transactional
    public WorkOrder create(WorkOrderForm form) {
        MaintenanceRequest request = requestRepository.findById(form.getMaintenanceRequestId())
                .orElseThrow(() -> new ResourceNotFoundException("yêu cầu bảo trì", form.getMaintenanceRequestId()));

        // BR01
        Quotation approved = quotationRepository
                .findByMaintenanceRequestIdAndStatus(request.getId(), QuotationStatus.APPROVED)
                .orElseThrow(() -> new BusinessException(
                        "Chưa có báo giá nào được duyệt cho yêu cầu này. Không thể tạo phiếu công việc (BR01)."));

        // Moi bao gia da duyet chi sinh ra mot phieu cong viec.
        if (workOrderRepository.findByQuotationId(approved.getId()).isPresent()) {
            throw new BusinessException("Báo giá này đã có phiếu công việc.");
        }

        // BR06
        if (form.getExpectedCompletionDate().isBefore(form.getStartDate())) {
            throw new BusinessException("Ngày dự kiến hoàn thành không được trước ngày bắt đầu (BR06).");
        }

        WorkOrder workOrder = new WorkOrder();
        workOrder.setWorkOrderCode(codeGenerator.nextWorkOrderCode());
        workOrder.setMaintenanceRequest(request);
        workOrder.setQuotation(approved);
        // BR09: contractor luon lay tu bao gia duoc duyet.
        workOrder.setContractor(approved.getContractor());
        workOrder.setStartDate(form.getStartDate());
        workOrder.setExpectedCompletionDate(form.getExpectedCompletionDate());
        workOrder.setWorkDescription(form.getWorkDescription());
        workOrder.setWarrantyUntil(form.getWarrantyUntil());
        workOrder.setStatus(WorkOrderStatus.NOT_STARTED);

        WorkOrder saved = workOrderRepository.save(workOrder);
        auditLogService.log(AuditAction.CREATE_WORK_ORDER, "MaintenanceRequest", request.getId(),
                "Tạo phiếu công việc " + saved.getWorkOrderCode()
                        + " giao cho " + saved.getContractor().getCompanyName());
        return saved;
    }

    /** BR10: bat dau cong viec -> yeu cau bao tri chuyen IN_PROGRESS. */
    @Override
    @Transactional
    public WorkOrder start(Long id) {
        WorkOrder workOrder = findById(id);
        if (workOrder.getStatus() != WorkOrderStatus.NOT_STARTED) {
            throw new BusinessException("Chỉ có thể bắt đầu phiếu công việc đang ở trạng thái chưa bắt đầu.");
        }
        workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
        WorkOrder saved = workOrderRepository.save(workOrder);

        updateRequestStatus(saved, RequestStatus.IN_PROGRESS);

        auditLogService.log(AuditAction.START_WORK_ORDER, "MaintenanceRequest",
                saved.getMaintenanceRequest().getId(),
                "Bắt đầu thực hiện " + saved.getWorkOrderCode());
        return saved;
    }

    @Override
    @Transactional
    public WorkOrder pause(Long id) {
        WorkOrder workOrder = findById(id);
        if (workOrder.getStatus() != WorkOrderStatus.IN_PROGRESS) {
            throw new BusinessException("Chỉ có thể tạm dừng công việc đang thực hiện.");
        }
        workOrder.setStatus(WorkOrderStatus.PAUSED);
        WorkOrder saved = workOrderRepository.save(workOrder);
        auditLogService.log(AuditAction.PAUSE_WORK_ORDER, "MaintenanceRequest",
                saved.getMaintenanceRequest().getId(),
                "Tạm dừng " + saved.getWorkOrderCode());
        return saved;
    }

    @Override
    @Transactional
    public WorkOrder resume(Long id) {
        WorkOrder workOrder = findById(id);
        if (workOrder.getStatus() != WorkOrderStatus.PAUSED) {
            throw new BusinessException("Chỉ có thể tiếp tục công việc đang tạm dừng.");
        }
        workOrder.setStatus(WorkOrderStatus.IN_PROGRESS);
        WorkOrder saved = workOrderRepository.save(workOrder);

        updateRequestStatus(saved, RequestStatus.IN_PROGRESS);

        auditLogService.log(AuditAction.RESUME_WORK_ORDER, "MaintenanceRequest",
                saved.getMaintenanceRequest().getId(),
                "Tiếp tục " + saved.getWorkOrderCode());
        return saved;
    }

    /** BR11: hoan thanh cong viec -> yeu cau bao tri chuyen WAITING_INSPECTION. */
    @Override
    @Transactional
    public WorkOrder complete(Long id, String result) {
        WorkOrder workOrder = findById(id);
        if (workOrder.getStatus() != WorkOrderStatus.IN_PROGRESS
                && workOrder.getStatus() != WorkOrderStatus.PAUSED) {
            throw new BusinessException(
                    "Chỉ có thể hoàn thành công việc đang thực hiện hoặc đang tạm dừng.");
        }

        LocalDate today = LocalDate.now();
        // BR06: ngay hoan thanh khong duoc truoc ngay bat dau.
        if (today.isBefore(workOrder.getStartDate())) {
            throw new BusinessException("Ngày hoàn thành không được trước ngày bắt đầu (BR06).");
        }

        workOrder.setStatus(WorkOrderStatus.COMPLETED);
        workOrder.setActualCompletionDate(today);
        if (StringUtils.hasText(result)) {
            workOrder.setResult(result);
        }
        WorkOrder saved = workOrderRepository.save(workOrder);

        updateRequestStatus(saved, RequestStatus.WAITING_INSPECTION);

        auditLogService.log(AuditAction.COMPLETE_WORK_ORDER, "MaintenanceRequest",
                saved.getMaintenanceRequest().getId(),
                "Hoàn thành " + saved.getWorkOrderCode() + ", chờ nghiệm thu");
        return saved;
    }

    @Override
    @Transactional
    public WorkOrder cancel(Long id) {
        WorkOrder workOrder = findById(id);
        if (workOrder.getStatus() == WorkOrderStatus.ACCEPTED) {
            throw new BusinessException("Không thể hủy phiếu công việc đã nghiệm thu.");
        }
        workOrder.setStatus(WorkOrderStatus.CANCELLED);
        WorkOrder saved = workOrderRepository.save(workOrder);

        // Tra yeu cau ve trang thai da duyet bao gia de co the tao phieu khac.
        MaintenanceRequest request = saved.getMaintenanceRequest();
        if (request.getStatus().isOpen()) {
            request.setStatus(RequestStatus.QUOTATION_APPROVED);
            requestRepository.save(request);
        }

        auditLogService.log(AuditAction.CANCEL_WORK_ORDER, "MaintenanceRequest", request.getId(),
                "Hủy phiếu công việc " + saved.getWorkOrderCode());
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public long countActive() {
        return workOrderRepository.countActiveWorkOrders();
    }

    /** Dong bo trang thai cua yeu cau bao tri theo phieu cong viec, neu yeu cau con mo. */
    private void updateRequestStatus(WorkOrder workOrder, RequestStatus newStatus) {
        MaintenanceRequest request = workOrder.getMaintenanceRequest();
        if (request.getStatus().isOpen()) {
            request.setStatus(newStatus);
            requestRepository.save(request);
        }
    }
}
