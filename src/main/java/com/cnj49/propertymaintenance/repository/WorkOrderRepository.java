package com.cnj49.propertymaintenance.repository;

import com.cnj49.propertymaintenance.entity.WorkOrder;
import com.cnj49.propertymaintenance.enums.WorkOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {

    List<WorkOrder> findByMaintenanceRequestIdOrderByCreatedAtDesc(Long maintenanceRequestId);

    List<WorkOrder> findByContractorIdOrderByStartDateDesc(Long contractorId);

    long countByContractorId(Long contractorId);

    boolean existsByContractorId(Long contractorId);

    Optional<WorkOrder> findByQuotationId(Long quotationId);

    /** Phieu cong viec dang chay cua mot yeu cau (chua huy, chua nghiem thu). */
    @Query("""
            SELECT w FROM WorkOrder w
            WHERE w.maintenanceRequest.id = :requestId
              AND w.status NOT IN (com.cnj49.propertymaintenance.enums.WorkOrderStatus.CANCELLED,
                                   com.cnj49.propertymaintenance.enums.WorkOrderStatus.ACCEPTED)
            ORDER BY w.createdAt DESC
            """)
    List<WorkOrder> findActiveByRequestId(@Param("requestId") Long requestId);

    @Query("""
            SELECT w FROM WorkOrder w
            WHERE (:keyword IS NULL OR LOWER(w.workOrderCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                    OR LOWER(w.workDescription) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                    OR LOWER(w.maintenanceRequest.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:contractorId IS NULL OR w.contractor.id = :contractorId)
              AND (:status IS NULL OR w.status = :status)
            """)
    Page<WorkOrder> search(@Param("keyword") String keyword,
                           @Param("contractorId") Long contractorId,
                           @Param("status") WorkOrderStatus status,
                           Pageable pageable);

    /** Dashboard: so cong viec dang thuc hien. */
    @Query("""
            SELECT COUNT(w) FROM WorkOrder w
            WHERE w.status IN (com.cnj49.propertymaintenance.enums.WorkOrderStatus.IN_PROGRESS,
                               com.cnj49.propertymaintenance.enums.WorkOrderStatus.PAUSED)
            """)
    long countActiveWorkOrders();

    /** Danh sach cho man hinh nghiem thu: cong viec da COMPLETED cho nghiem thu. */
    List<WorkOrder> findByStatusOrderByActualCompletionDateDesc(WorkOrderStatus status);

    /** Sinh ma WO-yyyy-xxxx. */
    @Query("SELECT MAX(CAST(SUBSTRING(w.workOrderCode, 9) AS integer)) FROM WorkOrder w WHERE w.workOrderCode LIKE CONCAT('WO-', :year, '-%')")
    Integer findMaxCodeSequenceByYear(@Param("year") String year);
}
