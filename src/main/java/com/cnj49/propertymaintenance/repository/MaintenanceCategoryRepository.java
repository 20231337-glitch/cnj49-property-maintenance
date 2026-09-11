package com.cnj49.propertymaintenance.repository;

import com.cnj49.propertymaintenance.entity.MaintenanceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaintenanceCategoryRepository extends JpaRepository<MaintenanceCategory, Long> {

    List<MaintenanceCategory> findByActiveTrueOrderByNameAsc();

    List<MaintenanceCategory> findAllByOrderByNameAsc();

    Optional<MaintenanceCategory> findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
