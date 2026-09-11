package com.cnj49.propertymaintenance.service;

import com.cnj49.propertymaintenance.entity.Property;
import com.cnj49.propertymaintenance.enums.PropertyStatus;
import com.cnj49.propertymaintenance.enums.PropertyType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/** Quan ly bat dong san. */
public interface PropertyService {

    Page<Property> search(String keyword, PropertyType type, PropertyStatus status, Pageable pageable);

    List<Property> findAll();

    Property findById(Long id);

    Property create(Property property);

    Property update(Long id, Property property);

    /** BR14: khong xoa duoc bat dong san dang co yeu cau bao tri. */
    void delete(Long id);

    long countAll();
}
