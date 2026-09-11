package com.cnj49.propertymaintenance.service.impl;

import com.cnj49.propertymaintenance.dto.UnitForm;
import com.cnj49.propertymaintenance.entity.Property;
import com.cnj49.propertymaintenance.entity.Unit;
import com.cnj49.propertymaintenance.enums.UnitStatus;
import com.cnj49.propertymaintenance.enums.UnitType;
import com.cnj49.propertymaintenance.exception.BusinessException;
import com.cnj49.propertymaintenance.exception.ResourceNotFoundException;
import com.cnj49.propertymaintenance.repository.MaintenanceRequestRepository;
import com.cnj49.propertymaintenance.repository.PropertyRepository;
import com.cnj49.propertymaintenance.repository.UnitRepository;
import com.cnj49.propertymaintenance.service.UnitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UnitServiceImpl implements UnitService {

    private final UnitRepository unitRepository;
    private final PropertyRepository propertyRepository;
    private final MaintenanceRequestRepository requestRepository;

    public UnitServiceImpl(UnitRepository unitRepository,
                           PropertyRepository propertyRepository,
                           MaintenanceRequestRepository requestRepository) {
        this.unitRepository = unitRepository;
        this.propertyRepository = propertyRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Unit> search(String keyword, Long propertyId, Integer floor,
                             UnitType type, UnitStatus status, Pageable pageable) {
        String normalized = StringUtils.hasText(keyword) ? keyword.trim() : null;
        return unitRepository.search(normalized, propertyId, floor, type, status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Unit> findByProperty(Long propertyId) {
        return unitRepository.findByPropertyIdOrderByFloorNumberAscCodeAsc(propertyId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Integer> findDistinctFloors() {
        return unitRepository.findDistinctFloors();
    }

    @Override
    @Transactional(readOnly = true)
    public Unit findById(Long id) {
        return unitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("căn/phòng", id));
    }

    @Override
    @Transactional(readOnly = true)
    public UnitForm toForm(Unit unit) {
        UnitForm form = new UnitForm();
        form.setId(unit.getId());
        form.setPropertyId(unit.getProperty().getId());
        form.setCode(unit.getCode());
        form.setName(unit.getName());
        form.setFloorNumber(unit.getFloorNumber());
        form.setUnitType(unit.getUnitType());
        form.setArea(unit.getArea());
        form.setStatus(unit.getStatus());
        form.setDescription(unit.getDescription());
        return form;
    }

    /** BR08: Unit bat buoc thuoc mot Property ton tai; ma khong duoc trung trong cung Property. */
    @Override
    @Transactional
    public Unit save(UnitForm form) {
        Property property = propertyRepository.findById(form.getPropertyId())
                .orElseThrow(() -> new BusinessException("Bất động sản không tồn tại (BR08)"));

        boolean duplicated = form.getId() == null
                ? unitRepository.existsByPropertyIdAndCode(property.getId(), form.getCode())
                : unitRepository.existsByPropertyIdAndCodeAndIdNot(property.getId(), form.getCode(), form.getId());
        if (duplicated) {
            throw new BusinessException(
                    "Mã căn/phòng '" + form.getCode() + "' đã tồn tại trong bất động sản này");
        }

        Unit unit = form.getId() == null ? new Unit() : findById(form.getId());
        unit.setProperty(property);
        unit.setCode(form.getCode());
        unit.setName(form.getName());
        unit.setFloorNumber(form.getFloorNumber());
        unit.setUnitType(form.getUnitType());
        unit.setArea(form.getArea());
        unit.setStatus(form.getStatus());
        unit.setDescription(form.getDescription());
        return unitRepository.save(unit);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Unit unit = findById(id);
        if (requestRepository.countByUnitId(id) > 0) {
            throw new BusinessException("Không thể xóa căn/phòng đang có yêu cầu bảo trì.");
        }
        unitRepository.delete(unit);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAll() {
        return unitRepository.count();
    }
}
