package com.cnj49.propertymaintenance.repository;

import com.cnj49.propertymaintenance.entity.Property;
import com.cnj49.propertymaintenance.enums.PropertyStatus;
import com.cnj49.propertymaintenance.enums.PropertyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    Optional<Property> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    List<Property> findAllByOrderByNameAsc();

    /**
     * Tim kiem + loc + phan trang cho man hinh danh sach bat dong san.
     * Tham so null nghia la "khong loc theo tieu chi do".
     */
    @Query("""
            SELECT p FROM Property p
            WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                    OR LOWER(p.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                    OR LOWER(p.address) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:propertyType IS NULL OR p.propertyType = :propertyType)
              AND (:status IS NULL OR p.status = :status)
            """)
    Page<Property> search(@Param("keyword") String keyword,
                          @Param("propertyType") PropertyType propertyType,
                          @Param("status") PropertyStatus status,
                          Pageable pageable);

    /** Sinh ma PROP-xxxx: lay so lon nhat hien co. */
    @Query("SELECT MAX(CAST(SUBSTRING(p.code, 6) AS integer)) FROM Property p WHERE p.code LIKE 'PROP-%'")
    Integer findMaxCodeSequence();
}
