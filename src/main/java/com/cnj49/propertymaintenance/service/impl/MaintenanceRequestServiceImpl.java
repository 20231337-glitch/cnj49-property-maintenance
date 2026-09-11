package com.cnj49.propertymaintenance.service.impl;

import com.cnj49.propertymaintenance.dto.MaintenanceRequestForm;
import com.cnj49.propertymaintenance.entity.*;
import com.cnj49.propertymaintenance.enums.AuditAction;
import com.cnj49.propertymaintenance.enums.Priority;
import com.cnj49.propertymaintenance.enums.RequestStatus;
import com.cnj49.propertymaintenance.exception.BusinessException;
import com.cnj49.propertymaintenance.exception.ResourceNotFoundException;
import com.cnj49.propertymaintenance.repository.*;
import com.cnj49.propertymaintenance.service.AuditLogService;
import com.cnj49.propertymaintenance.service.MaintenanceRequestService;
import com.cnj49.propertymaintenance.util.CodeGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class MaintenanceRequestServiceImpl implements MaintenanceRequestService {

    private final MaintenanceRequestRepository requestRepository;
    private final PropertyRepository propertyRepository;
    private final UnitRepository unitRepository;
    private final MaintenanceCategoryRepository categoryRepository;
    private final QuotationRepository quotationRepository;
    private final WorkOrderRepository workOrderRepository;
    private final CodeGenerator codeGenerator;
    private final AuditLogService auditLogService;

    public MaintenanceRequestServiceImpl(MaintenanceRequestRepository requestRepository,
                                         PropertyRepository propertyRepository,
                                         UnitRepository unitRepository,
                                         MaintenanceCategoryRepository categoryRepository,
                                         QuotationRepository quotationRepository,
                                         WorkOrderRepository workOrderRepository,
                                         CodeGenerator codeGenerator,
                                         AuditLogService auditLogService) {
        this.requestRepository = requestRepository;
        this.propertyRepository = propertyRepository;
        this.unitRepository = unitRepository;
        this.categoryRepository = categoryRepository;
        this.quotationRepository = quotationRepository;
        this.workOrderRepository = workOrderRepository;
        this.codeGenerator = codeGenerator;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaintenanceRequest> search(String keyword, Long propertyId, Long categoryId,
                                           Priority priority, RequestStatus status,
                                           LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        String normalized = StringUtils.hasText(keyword) ? keyword.trim() : null;
        return requestRepository.search(normalized, propertyId, categoryId, priority, status,
                fromDate, toDate, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceRequest findById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("yêu cầu bảo trì", id));
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceRequestForm toForm(MaintenanceRequest request) {
        MaintenanceRequestForm form = new MaintenanceRequestForm();
        form.setId(request.getId());
        form.setRequestCode(request.getRequestCode());
        form.setPropertyId(request.getProperty().getId());
        form.setUnitId(request.getUnit() != null ? request.getUnit().getId() : null);
        form.setCategoryId(request.getCategory().getId());
        form.setTitle(request.getTitle());
        form.setDescription(request.getDescription());
        form.setPriority(request.getPriority());
        form.setStatus(request.getStatus());
        form.setReportedDate(request.getReportedDate());
        form.setExpectedCompletionDate(request.getExpectedCompletionDate());
        form.setEstimatedCost(request.getEstimatedCost());
        form.setReportedBy(request.getReportedBy());
        form.setNotes(request.getNotes());
        return form;
    }

    @Override
    @Transactional
    public MaintenanceRequest create(MaintenanceRequestForm form) {
        MaintenanceRequest request = new MaintenanceRequest();
        applyForm(request, form);

        // TC02: yeu cau moi tao luon bat dau o trang thai NEW.
        request.setStatus(RequestStatus.NEW);
        request.setRequestCode(codeGenerator.nextMaintenanceRequestCode());

        MaintenanceRequest saved = requestRepository.save(request);
        auditLogService.log(AuditAction.CREATE_MAINTENANCE_REQUEST, "MaintenanceRequest", saved.getId(),
                "Tạo yêu cầu " + saved.getRequestCode() + ": " + saved.getTitle());
        return saved;
    }

    @Override
    @Transactional
    public MaintenanceRequest update(Long id, MaintenanceRequestForm form) {
        MaintenanceRequest request = findById(id);
        if (request.getStatus().isFinal()) {
            throw new BusinessException("Không thể sửa yêu cầu đã đóng hoặc đã hủy.");
        }
        applyForm(request, form);
        MaintenanceRequest saved = requestRepository.save(request);
        auditLogService.log(AuditAction.UPDATE_MAINTENANCE_REQUEST, "MaintenanceRequest", saved.getId(),
                "Cập nhật yêu cầu " + saved.getRequestCode());
        return saved;
    }

    /** Gan du lieu tu form vao entity, kiem tra rang buoc quan he va ngay thang. */
    private void applyForm(MaintenanceRequest request, MaintenanceRequestForm form) {
        Property property = propertyRepository.findById(form.getPropertyId())
                .orElseThrow(() -> new BusinessException("Bất động sản không tồn tại"));
        MaintenanceCategory category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new BusinessException("Hạng mục bảo trì không tồn tại"));

        Unit unit = null;
        if (form.getUnitId() != null) {
            unit = unitRepository.findById(form.getUnitId())
                    .orElseThrow(() -> new BusinessException("Căn/phòng không tồn tại"));
            // Can/phong phai thuoc dung bat dong san da chon.
            if (!unit.getProperty().getId().equals(property.getId())) {
                throw new BusinessException("Căn/phòng được chọn không thuộc bất động sản này.");
            }
        }

        // BR06: ngay du kien hoan thanh khong duoc truoc ngay phat hien.
        if (form.getExpectedCompletionDate() != null
                && form.getExpectedCompletionDate().isBefore(form.getReportedDate())) {
            throw new BusinessException("Ngày dự kiến hoàn thành không được trước ngày phát hiện (BR06).");
        }

        // BR07: so tien khong duoc am.
        if (form.getEstimatedCost() != null && form.getEstimatedCost().signum() < 0) {
            throw new BusinessException("Chi phí dự kiến không được âm (BR07).");
        }

        request.setProperty(property);
        request.setUnit(unit);
        request.setCategory(category);
        request.setTitle(form.getTitle());
        request.setDescription(form.getDescription());
        request.setPriority(form.getPriority());
        request.setReportedDate(form.getReportedDate());
        request.setExpectedCompletionDate(form.getExpectedCompletionDate());
        request.setEstimatedCost(form.getEstimatedCost() == null ? BigDecimal.ZERO : form.getEstimatedCost());
        request.setReportedBy(form.getReportedBy());
        request.setNotes(form.getNotes());
    }

    @Override
    @Transactional
    public MaintenanceRequest changeStatus(Long id, RequestStatus newStatus) {
        MaintenanceRequest request = findById(id);
        if (request.getStatus().isFinal()) {
            throw new BusinessException("Yêu cầu đã kết thúc, không thể đổi trạng thái.");
        }
        request.setStatus(newStatus);
        MaintenanceRequest saved = requestRepository.save(request);
        auditLogService.log(AuditAction.UPDATE_MAINTENANCE_REQUEST, "MaintenanceRequest", saved.getId(),
                "Chuyển trạng thái sang " + newStatus.getLabel());
        return saved;
    }

    /** BR05: chi dong duoc yeu cau khi da nghiem thu dat (trang thai COMPLETED). */
    @Override
    @Transactional
    public MaintenanceRequest close(Long id) {
        MaintenanceRequest request = findById(id);
        if (request.getStatus() != RequestStatus.COMPLETED) {
            throw new BusinessException(
                    "Chỉ được đóng yêu cầu đã hoàn thành (đã nghiệm thu đạt) - BR05.");
        }
        request.setStatus(RequestStatus.CLOSED);
        MaintenanceRequest saved = requestRepository.save(request);
        auditLogService.log(AuditAction.CLOSE_MAINTENANCE_REQUEST, "MaintenanceRequest", saved.getId(),
                "Đóng yêu cầu " + saved.getRequestCode());
        return saved;
    }

    @Override
    @Transactional
    public MaintenanceRequest cancel(Long id) {
        MaintenanceRequest request = findById(id);
        if (request.getStatus().isFinal() || request.getStatus() == RequestStatus.COMPLETED) {
            throw new BusinessException("Yêu cầu đã kết thúc, không thể hủy.");
        }
        if (!workOrderRepository.findActiveByRequestId(id).isEmpty()) {
            throw new BusinessException(
                    "Không thể hủy yêu cầu đang có phiếu công việc hoạt động. Hãy hủy phiếu công việc trước.");
        }
        request.setStatus(RequestStatus.CANCELLED);
        MaintenanceRequest saved = requestRepository.save(request);
        auditLogService.log(AuditAction.CANCEL_MAINTENANCE_REQUEST, "MaintenanceRequest", saved.getId(),
                "Hủy yêu cầu " + saved.getRequestCode());
        return saved;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        MaintenanceRequest request = findById(id);
        if (!workOrderRepository.findByMaintenanceRequestIdOrderByCreatedAtDesc(id).isEmpty()) {
            throw new BusinessException("Không thể xóa yêu cầu đã có phiếu công việc.");
        }
        // Xoa cac bao gia truoc de khong vi pham rang buoc khoa ngoai.
        quotationRepository.deleteAll(quotationRepository.findByMaintenanceRequestIdOrderByTotalAmountAsc(id));
        requestRepository.delete(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceRequest> findByProperty(Long propertyId) {
        return requestRepository.findByPropertyIdOrderByReportedDateDesc(propertyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceRequest> findRecent(int limit) {
        return requestRepository.findRecent(PageRequest.of(0, limit));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceRequest> findOverdue() {
        return requestRepository.findOverdueRequests(LocalDate.now());
    }
}
