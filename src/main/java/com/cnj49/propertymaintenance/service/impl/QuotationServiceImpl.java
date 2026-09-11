package com.cnj49.propertymaintenance.service.impl;

import com.cnj49.propertymaintenance.dto.QuotationForm;
import com.cnj49.propertymaintenance.entity.Contractor;
import com.cnj49.propertymaintenance.entity.MaintenanceRequest;
import com.cnj49.propertymaintenance.entity.Quotation;
import com.cnj49.propertymaintenance.enums.AuditAction;
import com.cnj49.propertymaintenance.enums.QuotationStatus;
import com.cnj49.propertymaintenance.enums.RequestStatus;
import com.cnj49.propertymaintenance.exception.BusinessException;
import com.cnj49.propertymaintenance.exception.ResourceNotFoundException;
import com.cnj49.propertymaintenance.repository.ContractorRepository;
import com.cnj49.propertymaintenance.repository.MaintenanceRequestRepository;
import com.cnj49.propertymaintenance.repository.QuotationRepository;
import com.cnj49.propertymaintenance.service.AuditLogService;
import com.cnj49.propertymaintenance.service.QuotationService;
import com.cnj49.propertymaintenance.util.CodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuotationServiceImpl implements QuotationService {

    private final QuotationRepository quotationRepository;
    private final MaintenanceRequestRepository requestRepository;
    private final ContractorRepository contractorRepository;
    private final CodeGenerator codeGenerator;
    private final AuditLogService auditLogService;

    public QuotationServiceImpl(QuotationRepository quotationRepository,
                                MaintenanceRequestRepository requestRepository,
                                ContractorRepository contractorRepository,
                                CodeGenerator codeGenerator,
                                AuditLogService auditLogService) {
        this.quotationRepository = quotationRepository;
        this.requestRepository = requestRepository;
        this.contractorRepository = contractorRepository;
        this.codeGenerator = codeGenerator;
        this.auditLogService = auditLogService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Quotation> findByRequest(Long requestId) {
        return quotationRepository.findByMaintenanceRequestIdOrderByTotalAmountAsc(requestId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Quotation> findByContractor(Long contractorId) {
        return quotationRepository.findByContractorIdOrderByQuotationDateDesc(contractorId);
    }

    @Override
    @Transactional(readOnly = true)
    public Quotation findById(Long id) {
        return quotationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("báo giá", id));
    }

    @Override
    @Transactional(readOnly = true)
    public QuotationForm toForm(Quotation quotation) {
        QuotationForm form = new QuotationForm();
        form.setId(quotation.getId());
        form.setMaintenanceRequestId(quotation.getMaintenanceRequest().getId());
        form.setContractorId(quotation.getContractor().getId());
        form.setQuotationDate(quotation.getQuotationDate());
        form.setMaterialCost(quotation.getMaterialCost());
        form.setLaborCost(quotation.getLaborCost());
        form.setOtherCost(quotation.getOtherCost());
        form.setEstimatedDays(quotation.getEstimatedDays());
        form.setDescription(quotation.getDescription());
        return form;
    }

    @Override
    @Transactional
    public Quotation create(QuotationForm form) {
        MaintenanceRequest request = loadRequest(form.getMaintenanceRequestId());
        if (request.getStatus().isFinal() || request.getStatus() == RequestStatus.COMPLETED) {
            throw new BusinessException("Yêu cầu đã kết thúc, không thể thêm báo giá.");
        }

        Quotation quotation = new Quotation();
        quotation.setMaintenanceRequest(request);
        applyForm(quotation, form);
        quotation.setStatus(QuotationStatus.PENDING);
        quotation.setQuotationCode(codeGenerator.nextQuotationCode());

        Quotation saved = quotationRepository.save(quotation);

        // Yeu cau chuyen sang trang thai cho bao gia neu dang o cac buoc dau.
        if (request.getStatus() == RequestStatus.NEW || request.getStatus() == RequestStatus.UNDER_REVIEW) {
            request.setStatus(RequestStatus.WAITING_QUOTATION);
            requestRepository.save(request);
        }

        auditLogService.log(AuditAction.CREATE_QUOTATION, "MaintenanceRequest", request.getId(),
                "Nhà thầu " + saved.getContractor().getCompanyName()
                        + " gửi báo giá " + saved.getQuotationCode()
                        + " - " + saved.getTotalAmount().toPlainString());
        return saved;
    }

    @Override
    @Transactional
    public Quotation update(Long id, QuotationForm form) {
        Quotation quotation = findById(id);
        if (quotation.getStatus() != QuotationStatus.PENDING) {
            throw new BusinessException("Chỉ được sửa báo giá đang ở trạng thái chờ duyệt.");
        }
        applyForm(quotation, form);
        return quotationRepository.save(quotation);
    }

    /** Gan du lieu form va LUON tinh lai totalAmount (nguoi dung khong duoc tu nhap). */
    private void applyForm(Quotation quotation, QuotationForm form) {
        Contractor contractor = contractorRepository.findById(form.getContractorId())
                .orElseThrow(() -> new BusinessException("Nhà thầu không tồn tại"));

        requireNonNegative(form.getMaterialCost(), "Chi phí vật tư");
        requireNonNegative(form.getLaborCost(), "Chi phí nhân công");
        requireNonNegative(form.getOtherCost(), "Chi phí khác");

        if (form.getEstimatedDays() == null || form.getEstimatedDays() < 1) {
            throw new BusinessException("Số ngày thực hiện phải lớn hơn 0.");
        }

        quotation.setContractor(contractor);
        quotation.setQuotationDate(form.getQuotationDate());
        quotation.setMaterialCost(form.getMaterialCost());
        quotation.setLaborCost(form.getLaborCost());
        quotation.setOtherCost(form.getOtherCost());
        quotation.setEstimatedDays(form.getEstimatedDays());
        quotation.setDescription(form.getDescription());
        quotation.recalculateTotal();
    }

    /** BR07: so tien khong duoc am. */
    private void requireNonNegative(BigDecimal value, String fieldLabel) {
        if (value == null || value.signum() < 0) {
            throw new BusinessException(fieldLabel + " không được âm (BR07).");
        }
    }

    /**
     * BR02 + BR03: duyet mot bao gia.
     *
     * - Khong duoc duyet neu yeu cau da co mot bao gia APPROVED khac (BR02).
     * - Sau khi duyet, toan bo bao gia PENDING con lai chuyen REJECTED (BR03).
     * - Yeu cau bao tri chuyen sang QUOTATION_APPROVED.
     */
    @Override
    @Transactional
    public Quotation approve(Long id) {
        Quotation quotation = findById(id);
        MaintenanceRequest request = quotation.getMaintenanceRequest();

        if (quotation.getStatus() == QuotationStatus.APPROVED) {
            throw new BusinessException("Báo giá này đã được duyệt.");
        }
        if (quotation.getStatus() == QuotationStatus.REJECTED) {
            throw new BusinessException("Báo giá đã bị từ chối, không thể duyệt.");
        }
        if (request.getStatus().isFinal() || request.getStatus() == RequestStatus.COMPLETED) {
            throw new BusinessException("Yêu cầu đã kết thúc, không thể duyệt báo giá.");
        }

        // BR02: moi yeu cau chi duoc co toi da MOT bao gia APPROVED tai cung thoi diem.
        boolean alreadyApproved = quotationRepository
                .existsByMaintenanceRequestIdAndStatus(request.getId(), QuotationStatus.APPROVED);
        if (alreadyApproved) {
            throw new BusinessException(
                    "Yêu cầu này đã có một báo giá được duyệt. Không thể duyệt thêm báo giá thứ hai (BR02).");
        }

        quotation.setStatus(QuotationStatus.APPROVED);
        quotation.setApprovedAt(LocalDateTime.now());
        Quotation saved = quotationRepository.save(quotation);

        // BR03: cac bao gia con lai cua yeu cau nay chuyen sang REJECTED.
        List<Quotation> others = quotationRepository
                .findByMaintenanceRequestIdOrderByTotalAmountAsc(request.getId());
        for (Quotation other : others) {
            if (!other.getId().equals(saved.getId()) && other.getStatus() == QuotationStatus.PENDING) {
                other.setStatus(QuotationStatus.REJECTED);
                quotationRepository.save(other);
            }
        }

        request.setStatus(RequestStatus.QUOTATION_APPROVED);
        request.setEstimatedCost(saved.getTotalAmount());
        requestRepository.save(request);

        auditLogService.log(AuditAction.APPROVE_QUOTATION, "MaintenanceRequest", request.getId(),
                "Duyệt báo giá " + saved.getQuotationCode()
                        + " của " + saved.getContractor().getCompanyName());
        return saved;
    }

    @Override
    @Transactional
    public Quotation reject(Long id) {
        Quotation quotation = findById(id);
        if (quotation.getStatus() == QuotationStatus.APPROVED) {
            throw new BusinessException("Không thể từ chối báo giá đã được duyệt.");
        }
        quotation.setStatus(QuotationStatus.REJECTED);
        Quotation saved = quotationRepository.save(quotation);
        auditLogService.log(AuditAction.REJECT_QUOTATION, "MaintenanceRequest",
                saved.getMaintenanceRequest().getId(),
                "Từ chối báo giá " + saved.getQuotationCode());
        return saved;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Quotation quotation = findById(id);
        if (quotation.getStatus() == QuotationStatus.APPROVED) {
            throw new BusinessException("Không thể xóa báo giá đã được duyệt.");
        }
        quotationRepository.delete(quotation);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Quotation> findApprovedByRequest(Long requestId) {
        return quotationRepository.findByMaintenanceRequestIdAndStatus(requestId, QuotationStatus.APPROVED);
    }

    private MaintenanceRequest loadRequest(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("yêu cầu bảo trì", requestId));
    }
}
