package com.cnj49.propertymaintenance.service;

import com.cnj49.propertymaintenance.entity.MaintenanceCategory;

import java.util.List;

/** Quan ly danh muc hang muc bao tri. */
public interface MaintenanceCategoryService {

    List<MaintenanceCategory> findAll();

    List<MaintenanceCategory> findActive();

    MaintenanceCategory findById(Long id);

    MaintenanceCategory save(MaintenanceCategory category);

    void delete(Long id);
}
