package com.cnj49.propertymaintenance.repository;

import com.cnj49.propertymaintenance.entity.Contractor;
import com.cnj49.propertymaintenance.enums.ContractorStatus;
import com.cnj49.propertymaintenance.enums.Specialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractorRepository extends JpaRepository<Contractor, Long> {

    List<Contractor> findByStatusOrderByCompanyNameAsc(ContractorStatus status);

    List<Contractor> findAllByOrderByCompanyNameAsc();

    boolean existsByContractorCode(String contractorCode);

    @Query("""
            SELECT c FROM Contractor c
            WHERE (:keyword IS NULL OR LOWER(c.companyName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                    OR LOWER(c.contractorCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                    OR LOWER(c.contactPerson) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                    OR c.phone LIKE CONCAT('%', :keyword, '%'))
              AND (:specialization IS NULL OR c.specialization = :specialization)
              AND (:status IS NULL OR c.status = :status)
            """)
    Page<Contractor> search(@Param("keyword") String keyword,
                            @Param("specialization") Specialization specialization,
                            @Param("status") ContractorStatus status,
                            Pageable pageable);

    /** Sinh ma CTR-xxxx. */
    @Query("SELECT MAX(CAST(SUBSTRING(c.contractorCode, 5) AS integer)) FROM Contractor c WHERE c.contractorCode LIKE 'CTR-%'")
    Integer findMaxCodeSequence();
}
