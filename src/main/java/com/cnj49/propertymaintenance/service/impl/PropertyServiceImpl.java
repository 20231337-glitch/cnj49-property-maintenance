package com.cnj49.propertymaintenance.service.impl;

import com.cnj49.propertymaintenance.entity.Property;
import com.cnj49.propertymaintenance.enums.PropertyStatus;
import com.cnj49.propertymaintenance.enums.PropertyType;
import com.cnj49.propertymaintenance.exception.BusinessException;
import com.cnj49.propertymaintenance.exception.ResourceNotFoundException;
import com.cnj49.propertymaintenance.repository.ExpenseRepository;
import com.cnj49.propertymaintenance.repository.MaintenanceRequestRepository;
import com.cnj49.propertymaintenance.repository.PropertyRepository;
import com.cnj49.propertymaintenance.repository.UnitRepository;
import com.cnj49.propertymaintenance.service.PropertyService;
import com.cnj49.propertymaintenance.util.CodeGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final UnitRepository unitRepository;
    private final MaintenanceRequestRepository requestRepository;
    private final ExpenseRepository expenseRepository;
    private final CodeGenerator codeGenerator;

    public PropertyServiceImpl(PropertyRepository propertyRepository,
                               UnitRepository unitRepository,
                               MaintenanceRequestRepository requestRepository,
                               ExpenseRepository expenseRepository,
                               CodeGenerator codeGenerator) {
        this.propertyRepository = propertyRepository;
        this.unitRepository = unitRepository;
        this.requestRepository = requestRepository;
        this.expenseRepository = expenseRepository;
        this.codeGenerator = codeGenerator;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Property> search(String keyword, PropertyType type, PropertyStatus status, Pageable pageable) {
        return propertyRepository.search(normalize(keyword), type, status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Property> findAll() {
        return propertyRepository.findAllByOrderByNameAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public Property findById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("bất động sản", id));
    }

    @Override
    @Transactional
    public Property create(Property property) {
        // Ma do he thong sinh, nguoi dung khong tu nhap.
        property.setCode(codeGenerator.nextPropertyCode());
        property.setId(null);
        return propertyRepository.save(property);
    }

    @Override
    @Transactional
    public Property update(Long id, Property input) {
        Property existing = findById(id);
        existing.setName(input.getName());
        existing.setPropertyType(input.getPropertyType());
        existing.setAddress(input.getAddress());
        existing.setNumberOfFloors(input.getNumberOfFloors());
        existing.setNumberOfUnits(input.getNumberOfUnits());
        existing.setArea(input.getArea());
        existing.setOperationDate(input.getOperationDate());
        existing.setStatus(input.getStatus());
        existing.setDescription(input.getDescription());
        return propertyRepository.save(existing);
    }

    /** BR14: khong xoa bat dong san dang co yeu cau bao tri hoac du lieu lien quan. */
    @Override
    @Transactional
    public void delete(Long id) {
        Property property = findById(id);

        if (requestRepository.countByPropertyId(id) > 0) {
            throw new BusinessException(
                    "Không thể xóa bất động sản đang có yêu cầu bảo trì (BR14).");
        }
        if (unitRepository.countByPropertyId(id) > 0) {
            throw new BusinessException(
                    "Không thể xóa bất động sản đang có căn/phòng. Hãy xóa các căn/phòng trước.");
        }
        if (expenseRepository.sumByPropertyId(id).signum() > 0) {
            throw new BusinessException(
                    "Không thể xóa bất động sản đã phát sinh chi phí vận hành.");
        }
        propertyRepository.delete(property);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAll() {
        return propertyRepository.count();
    }

    private String normalize(String keyword) {
        return StringUtils.hasText(keyword) ? keyword.trim() : null;
    }
}
