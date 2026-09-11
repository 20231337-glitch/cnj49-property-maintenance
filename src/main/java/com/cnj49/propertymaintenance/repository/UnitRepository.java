package com.cnj49.propertymaintenance.repository;

import com.cnj49.propertymaintenance.entity.Unit;
import com.cnj49.propertymaintenance.enums.UnitStatus;
import com.cnj49.propertymaintenance.enums.UnitType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    List<Unit> findByPropertyIdOrderByFloorNumberAscCodeAsc(Long propertyId);

    long countByPropertyId(Long propertyId);

    boolean existsByPropertyIdAndCode(Long propertyId, String code);

    boolean existsByPropertyIdAndCodeAndIdNot(Long propertyId, String code, Long id);

    @Query("""
            SELECT u FROM Unit u
            WHERE (:keyword IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                                    OR LOWER(u.code) LIKE LOWER(CONCAT('%', :keyword, '%')))
              AND (:propertyId IS NULL OR u.property.id = :propertyId)
              AND (:floorNumber IS NULL OR u.floorNumber = :floorNumber)
              AND (:unitType IS NULL OR u.unitType = :unitType)
              AND (:status IS NULL OR u.status = :status)
            """)
    Page<Unit> search(@Param("keyword") String keyword,
                      @Param("propertyId") Long propertyId,
                      @Param("floorNumber") Integer floorNumber,
                      @Param("unitType") UnitType unitType,
                      @Param("status") UnitStatus status,
                      Pageable pageable);

    /** Danh sach tang dang co, dung do du lieu cho dropdown loc theo tang. */
    @Query("SELECT DISTINCT u.floorNumber FROM Unit u ORDER BY u.floorNumber ASC")
    List<Integer> findDistinctFloors();
}
