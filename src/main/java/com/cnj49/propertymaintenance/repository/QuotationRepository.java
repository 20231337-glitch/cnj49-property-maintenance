package com.cnj49.propertymaintenance.repository;

import com.cnj49.propertymaintenance.entity.Quotation;
import com.cnj49.propertymaintenance.enums.QuotationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {

    List<Quotation> findByMaintenanceRequestIdOrderByTotalAmountAsc(Long maintenanceRequestId);

    List<Quotation> findByContractorIdOrderByQuotationDateDesc(Long contractorId);

    long countByContractorId(Long contractorId);

    /** BR02: tim bao gia dang duoc duyet cua mot yeu cau (toi da mot). */
    Optional<Quotation> findByMaintenanceRequestIdAndStatus(Long maintenanceRequestId, QuotationStatus status);

    boolean existsByMaintenanceRequestIdAndStatus(Long maintenanceRequestId, QuotationStatus status);

    List<Quotation> findByMaintenanceRequestIdAndStatusNot(Long maintenanceRequestId, QuotationStatus status);

    /** Sinh ma QT-yyyy-xxxx. */
    @Query("SELECT MAX(CAST(SUBSTRING(q.quotationCode, 9) AS integer)) FROM Quotation q WHERE q.quotationCode LIKE CONCAT('QT-', :year, '-%')")
    Integer findMaxCodeSequenceByYear(@Param("year") String year);
}
