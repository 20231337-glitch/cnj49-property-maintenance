package com.cnj49.propertymaintenance.service;

import com.cnj49.propertymaintenance.dto.ContractorStatistics;
import com.cnj49.propertymaintenance.entity.Contractor;
import com.cnj49.propertymaintenance.enums.ContractorStatus;
import com.cnj49.propertymaintenance.enums.Specialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/** Quan ly nha thau. */
public interface ContractorService {

    Page<Contractor> search(String keyword, Specialization specialization, ContractorStatus status, Pageable pageable);

    List<Contractor> findAll();

    List<Contractor> findActive();

    Contractor findById(Long id);

    Contractor create(Contractor contractor);

    Contractor update(Long id, Contractor contractor);

    /** BR15: khong xoa nha thau dang duoc tham chieu boi WorkOrder. */
    void delete(Long id);

    ContractorStatistics getStatistics(Long contractorId);
}
