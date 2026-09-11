package com.cnj49.propertymaintenance.repository;

import com.cnj49.propertymaintenance.entity.Inspection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Long> {

    List<Inspection> findByWorkOrderIdOrderByInspectionDateDesc(Long workOrderId);

    Page<Inspection> findAllByOrderByInspectionDateDesc(Pageable pageable);

    long countByWorkOrderId(Long workOrderId);
}
