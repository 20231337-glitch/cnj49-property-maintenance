package com.cnj49.propertymaintenance.service.impl;

import com.cnj49.propertymaintenance.dto.ContractorStatistics;
import com.cnj49.propertymaintenance.entity.Contractor;
import com.cnj49.propertymaintenance.enums.ContractorStatus;
import com.cnj49.propertymaintenance.enums.Specialization;
import com.cnj49.propertymaintenance.exception.BusinessException;
import com.cnj49.propertymaintenance.exception.ResourceNotFoundException;
import com.cnj49.propertymaintenance.repository.ContractorRepository;
import com.cnj49.propertymaintenance.repository.ExpenseRepository;
import com.cnj49.propertymaintenance.repository.QuotationRepository;
import com.cnj49.propertymaintenance.repository.WorkOrderRepository;
import com.cnj49.propertymaintenance.service.ContractorService;
import com.cnj49.propertymaintenance.util.CodeGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ContractorServiceImpl implements ContractorService {

    private final ContractorRepository contractorRepository;
    private final QuotationRepository quotationRepository;
    private final WorkOrderRepository workOrderRepository;
    private final ExpenseRepository expenseRepository;
    private final CodeGenerator codeGenerator;

    public ContractorServiceImpl(ContractorRepository contractorRepository,
                                 QuotationRepository quotationRepository,
                                 WorkOrderRepository workOrderRepository,
                                 ExpenseRepository expenseRepository,
                                 CodeGenerator codeGenerator) {
        this.contractorRepository = contractorRepository;
        this.quotationRepository = quotationRepository;
        this.workOrderRepository = workOrderRepository;
        this.expenseRepository = expenseRepository;
        this.codeGenerator = codeGenerator;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Contractor> search(String keyword, Specialization specialization,
                                   ContractorStatus status, Pageable pageable) {
        String normalized = StringUtils.hasText(keyword) ? keyword.trim() : null;
        return contractorRepository.search(normalized, specialization, status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contractor> findAll() {
        return contractorRepository.findAllByOrderByCompanyNameAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contractor> findActive() {
        return contractorRepository.findByStatusOrderByCompanyNameAsc(ContractorStatus.ACTIVE);
    }

    @Override
    @Transactional(readOnly = true)
    public Contractor findById(Long id) {
        return contractorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("nhà thầu", id));
    }

    @Override
    @Transactional
    public Contractor create(Contractor contractor) {
        contractor.setId(null);
        contractor.setContractorCode(codeGenerator.nextContractorCode());
        validateRating(contractor.getRating());
        return contractorRepository.save(contractor);
    }

    @Override
    @Transactional
    public Contractor update(Long id, Contractor input) {
        Contractor existing = findById(id);
        validateRating(input.getRating());
        existing.setCompanyName(input.getCompanyName());
        existing.setContactPerson(input.getContactPerson());
        existing.setPhone(input.getPhone());
        existing.setEmail(input.getEmail());
        existing.setAddress(input.getAddress());
        existing.setTaxCode(input.getTaxCode());
        existing.setSpecialization(input.getSpecialization());
        existing.setRating(input.getRating());
        existing.setStatus(input.getStatus());
        existing.setNotes(input.getNotes());
        return contractorRepository.save(existing);
    }

    /** BR15: khong xoa nha thau dang duoc tham chieu boi WorkOrder hoac bao gia. */
    @Override
    @Transactional
    public void delete(Long id) {
        Contractor contractor = findById(id);
        if (workOrderRepository.existsByContractorId(id)) {
            throw new BusinessException(
                    "Không thể xóa nhà thầu đang được tham chiếu bởi phiếu công việc (BR15).");
        }
        if (quotationRepository.countByContractorId(id) > 0) {
            throw new BusinessException(
                    "Không thể xóa nhà thầu đã có báo giá. Hãy chuyển sang trạng thái ngừng hợp tác.");
        }
        if (expenseRepository.countByContractorId(id) > 0) {
            throw new BusinessException("Không thể xóa nhà thầu đã phát sinh chi phí.");
        }
        contractorRepository.delete(contractor);
    }

    @Override
    @Transactional(readOnly = true)
    public ContractorStatistics getStatistics(Long contractorId) {
        ContractorStatistics stats = new ContractorStatistics();
        stats.setQuotationCount(quotationRepository.countByContractorId(contractorId));
        stats.setWorkOrderCount(workOrderRepository.countByContractorId(contractorId));
        stats.setTotalPaid(expenseRepository.sumByContractorId(contractorId));
        return stats;
    }

    private void validateRating(BigDecimal rating) {
        if (rating == null || rating.signum() < 0 || rating.compareTo(new BigDecimal("5")) > 0) {
            throw new BusinessException("Đánh giá nhà thầu phải nằm trong khoảng 0 đến 5.");
        }
    }
}
