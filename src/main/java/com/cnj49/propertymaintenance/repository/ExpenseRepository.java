package com.cnj49.propertymaintenance.repository;

import com.cnj49.propertymaintenance.entity.Expense;
import com.cnj49.propertymaintenance.enums.ExpenseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    long countByContractorId(Long contractorId);

    List<Expense> findByWorkOrderId(Long workOrderId);

    @Query("""
            SELECT e FROM Expense e
            WHERE (:keyword IS NULL OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                    OR LOWER(e.expenseCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                    OR LOWER(e.referenceNumber) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:propertyId IS NULL OR e.property.id = :propertyId)
              AND (:contractorId IS NULL OR e.contractor.id = :contractorId)
              AND (:expenseType IS NULL OR e.expenseType = :expenseType)
              AND (:fromDate IS NULL OR e.expenseDate >= :fromDate)
              AND (:toDate IS NULL OR e.expenseDate <= :toDate)
            """)
    Page<Expense> search(@Param("keyword") String keyword,
                         @Param("propertyId") Long propertyId,
                         @Param("contractorId") Long contractorId,
                         @Param("expenseType") ExpenseType expenseType,
                         @Param("fromDate") LocalDate fromDate,
                         @Param("toDate") LocalDate toDate,
                         Pageable pageable);

    /** Tong chi phi ung voi cung bo loc cua man hinh danh sach. */
    @Query("""
            SELECT COALESCE(SUM(e.amount), 0) FROM Expense e
            WHERE (:keyword IS NULL OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                    OR LOWER(e.expenseCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                    OR LOWER(e.referenceNumber) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:propertyId IS NULL OR e.property.id = :propertyId)
              AND (:contractorId IS NULL OR e.contractor.id = :contractorId)
              AND (:expenseType IS NULL OR e.expenseType = :expenseType)
              AND (:fromDate IS NULL OR e.expenseDate >= :fromDate)
              AND (:toDate IS NULL OR e.expenseDate <= :toDate)
            """)
    BigDecimal sumBySearch(@Param("keyword") String keyword,
                           @Param("propertyId") Long propertyId,
                           @Param("contractorId") Long contractorId,
                           @Param("expenseType") ExpenseType expenseType,
                           @Param("fromDate") LocalDate fromDate,
                           @Param("toDate") LocalDate toDate);

    /** Dashboard card: tong chi phi trong khoang ngay (dung cho thang hien tai). */
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.expenseDate BETWEEN :fromDate AND :toDate")
    BigDecimal sumBetween(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.property.id = :propertyId")
    BigDecimal sumByPropertyId(@Param("propertyId") Long propertyId);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.contractor.id = :contractorId")
    BigDecimal sumByContractorId(@Param("contractorId") Long contractorId);

    /** Chart 1 / Bao cao 1: chi phi theo thang (nam, thang, tong). */
    @Query("""
            SELECT YEAR(e.expenseDate), MONTH(e.expenseDate), COALESCE(SUM(e.amount), 0)
            FROM Expense e
            WHERE (:fromDate IS NULL OR e.expenseDate >= :fromDate)
              AND (:toDate IS NULL OR e.expenseDate <= :toDate)
            GROUP BY YEAR(e.expenseDate), MONTH(e.expenseDate)
            ORDER BY YEAR(e.expenseDate), MONTH(e.expenseDate)
            """)
    List<Object[]> sumGroupByMonth(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

    /** Chart 3 / Bao cao 2: chi phi theo bat dong san. */
    @Query("""
            SELECT e.property.name, COALESCE(SUM(e.amount), 0) FROM Expense e
            WHERE (:fromDate IS NULL OR e.expenseDate >= :fromDate)
              AND (:toDate IS NULL OR e.expenseDate <= :toDate)
              AND (:propertyId IS NULL OR e.property.id = :propertyId)
            GROUP BY e.property.name ORDER BY COALESCE(SUM(e.amount), 0) DESC
            """)
    List<Object[]> sumGroupByProperty(@Param("fromDate") LocalDate fromDate,
                                      @Param("toDate") LocalDate toDate,
                                      @Param("propertyId") Long propertyId);

    /** Bao cao 3: chi phi theo loai. */
    @Query("""
            SELECT e.expenseType, COALESCE(SUM(e.amount), 0) FROM Expense e
            WHERE (:fromDate IS NULL OR e.expenseDate >= :fromDate)
              AND (:toDate IS NULL OR e.expenseDate <= :toDate)
              AND (:propertyId IS NULL OR e.property.id = :propertyId)
            GROUP BY e.expenseType ORDER BY COALESCE(SUM(e.amount), 0) DESC
            """)
    List<Object[]> sumGroupByType(@Param("fromDate") LocalDate fromDate,
                                  @Param("toDate") LocalDate toDate,
                                  @Param("propertyId") Long propertyId);

    /** Bao cao 5: nha thau co tong gia tri cong viec lon nhat. */
    @Query("""
            SELECT e.contractor.companyName, COALESCE(SUM(e.amount), 0), COUNT(e)
            FROM Expense e
            WHERE e.contractor IS NOT NULL
              AND (:fromDate IS NULL OR e.expenseDate >= :fromDate)
              AND (:toDate IS NULL OR e.expenseDate <= :toDate)
              AND (:contractorId IS NULL OR e.contractor.id = :contractorId)
            GROUP BY e.contractor.companyName ORDER BY COALESCE(SUM(e.amount), 0) DESC
            """)
    List<Object[]> sumGroupByContractor(@Param("fromDate") LocalDate fromDate,
                                        @Param("toDate") LocalDate toDate,
                                        @Param("contractorId") Long contractorId);

    /** Sinh ma EXP-yyyy-xxxx. */
    @Query("SELECT MAX(CAST(SUBSTRING(e.expenseCode, 10) AS integer)) FROM Expense e WHERE e.expenseCode LIKE CONCAT('EXP-', :year, '-%')")
    Integer findMaxCodeSequenceByYear(@Param("year") String year);
}
