package com.cnj49.propertymaintenance.service;

import com.cnj49.propertymaintenance.dto.UnitForm;
import com.cnj49.propertymaintenance.entity.Unit;
import com.cnj49.propertymaintenance.enums.UnitStatus;
import com.cnj49.propertymaintenance.enums.UnitType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/** Quan ly can / phong / khu vuc. */
public interface UnitService {

    Page<Unit> search(String keyword, Long propertyId, Integer floor, UnitType type, UnitStatus status, Pageable pageable);

    List<Unit> findByProperty(Long propertyId);

    List<Integer> findDistinctFloors();

    Unit findById(Long id);

    UnitForm toForm(Unit unit);

    Unit save(UnitForm form);

    void delete(Long id);

    long countAll();
}
