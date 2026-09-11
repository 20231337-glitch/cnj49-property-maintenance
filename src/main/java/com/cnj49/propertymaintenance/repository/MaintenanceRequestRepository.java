package com.cnj49.propertymaintenance.repository;

import com.cnj49.propertymaintenance.entity.MaintenanceRequest;
import com.cnj49.propertymaintenance.enums.Priority;
import com.cnj49.propertymaintenance.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Long> {

    List<MaintenanceRequest> findByPropertyIdOrderByReportedDateDesc(Long propertyId);

    long countByPropertyId(Long propertyId);

    long countByCategoryId(Long categoryId);

    long countByUnitId(Long unitId);

    /**
     * Tim kiem tong hop cho man hinh /maintenance:
     * keyword, property, category, priority, status, khoang ngay phat hien.
     */
    @Query("""
            SELECT r FROM MaintenanceRequest r
            WHERE (:keyword IS NULL OR LOWER(r.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                    OR LOWER(r.requestCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                    OR LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:propertyId IS NULL OR r.property.id = :propertyId)
              AND (:categoryId IS NULL OR r.category.id = :categoryId)
              AND (:priority IS NULL OR r.priority = :priority)
              AND (:status IS NULL OR r.status = :status)
              AND (:fromDate IS NULL OR r.reportedDate >= :fromDate)
              AND (:toDate IS NULL OR r.reportedDate <= :toDate)
            """)
    Page<MaintenanceRequest> search(@Param("keyword") String keyword,
                                    @Param("propertyId") Long propertyId,
                                    @Param("categoryId") Long categoryId,
                                    @Param("priority") Priority priority,
                                    @Param("status") RequestStatus status,
                                    @Param("fromDate") LocalDate fromDate,
                                    @Param("toDate") LocalDate toDate,
                                    Pageable pageable);

    /** Dashboard: so yeu cau dang mo (chua COMPLETED/CLOSED/CANCELLED). */
    @Query("""
            SELECT COUNT(r) FROM MaintenanceRequest r
            WHERE r.status NOT IN (com.cnj49.propertymaintenance.enums.RequestStatus.COMPLETED,
                                   com.cnj49.propertymaintenance.enums.RequestStatus.CLOSED,
                                   com.cnj49.propertymaintenance.enums.RequestStatus.CANCELLED)
            """)
    long countOpenRequests();

    /** Dashboard: so su co khan cap con dang mo. */
    @Query("""
            SELECT COUNT(r) FROM MaintenanceRequest r
            WHERE r.priority = com.cnj49.propertymaintenance.enums.Priority.URGENT
              AND r.status NOT IN (com.cnj49.propertymaintenance.enums.RequestStatus.COMPLETED,
                                   com.cnj49.propertymaintenance.enums.RequestStatus.CLOSED,
                                   com.cnj49.propertymaintenance.enums.RequestStatus.CANCELLED)
            """)
    long countUrgentOpenRequests();

    /** Chart 2 / Bao cao 6: so yeu cau theo trang thai. */
    @Query("SELECT r.status, COUNT(r) FROM MaintenanceRequest r GROUP BY r.status ORDER BY r.status")
    List<Object[]> countGroupByStatus();

    /** Chart 4 / Bao cao 4: so su co theo hang muc. */
    @Query("""
            SELECT r.category.name, COUNT(r) FROM MaintenanceRequest r
            GROUP BY r.category.name ORDER BY COUNT(r) DESC
            """)
    List<Object[]> countGroupByCategory();

    /** Bao cao 4 co loc theo khoang ngay. */
    @Query("""
            SELECT r.category.name, COUNT(r) FROM MaintenanceRequest r
            WHERE (:fromDate IS NULL OR r.reportedDate >= :fromDate)
              AND (:toDate IS NULL OR r.reportedDate <= :toDate)
            GROUP BY r.category.name ORDER BY COUNT(r) DESC
            """)
    List<Object[]> countGroupByCategoryBetween(@Param("fromDate") LocalDate fromDate,
                                               @Param("toDate") LocalDate toDate);

    /** Bao cao 6 co loc theo khoang ngay. */
    @Query("""
            SELECT r.status, COUNT(r) FROM MaintenanceRequest r
            WHERE (:fromDate IS NULL OR r.reportedDate >= :fromDate)
              AND (:toDate IS NULL OR r.reportedDate <= :toDate)
            GROUP BY r.status ORDER BY r.status
            """)
    List<Object[]> countGroupByStatusBetween(@Param("fromDate") LocalDate fromDate,
                                             @Param("toDate") LocalDate toDate);

    /** Bao cao 7: cac yeu cau qua han (vuot ngay du kien ma chua ket thuc). */
    @Query("""
            SELECT r FROM MaintenanceRequest r
            WHERE r.expectedCompletionDate IS NOT NULL
              AND r.expectedCompletionDate < :today
              AND r.status NOT IN (com.cnj49.propertymaintenance.enums.RequestStatus.COMPLETED,
                                   com.cnj49.propertymaintenance.enums.RequestStatus.CLOSED,
                                   com.cnj49.propertymaintenance.enums.RequestStatus.CANCELLED)
            ORDER BY r.expectedCompletionDate ASC
            """)
    List<MaintenanceRequest> findOverdueRequests(@Param("today") LocalDate today);

    /** Danh sach moi nhat cho dashboard. */
    @Query("SELECT r FROM MaintenanceRequest r ORDER BY r.createdAt DESC")
    List<MaintenanceRequest> findRecent(Pageable pageable);

    /** Sinh ma MR-yyyy-xxxx trong pham vi mot nam. */
    @Query("SELECT MAX(CAST(SUBSTRING(r.requestCode, 9) AS integer)) FROM MaintenanceRequest r WHERE r.requestCode LIKE CONCAT('MR-', :year, '-%')")
    Integer findMaxCodeSequenceByYear(@Param("year") String year);
}
