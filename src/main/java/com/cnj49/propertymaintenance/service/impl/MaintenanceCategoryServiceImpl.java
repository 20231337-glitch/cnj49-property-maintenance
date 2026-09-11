package com.cnj49.propertymaintenance.service.impl;

import com.cnj49.propertymaintenance.entity.MaintenanceCategory;
import com.cnj49.propertymaintenance.exception.BusinessException;
import com.cnj49.propertymaintenance.exception.ResourceNotFoundException;
import com.cnj49.propertymaintenance.repository.MaintenanceCategoryRepository;
import com.cnj49.propertymaintenance.repository.MaintenanceRequestRepository;
import com.cnj49.propertymaintenance.service.MaintenanceCategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MaintenanceCategoryServiceImpl implements MaintenanceCategoryService {

    private final MaintenanceCategoryRepository categoryRepository;
    private final MaintenanceRequestRepository requestRepository;

    public MaintenanceCategoryServiceImpl(MaintenanceCategoryRepository categoryRepository,
                                          MaintenanceRequestRepository requestRepository) {
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceCategory> findAll() {
        return categoryRepository.findAllByOrderByNameAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaintenanceCategory> findActive() {
        return categoryRepository.findByActiveTrueOrderByNameAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public MaintenanceCategory findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("hạng mục bảo trì", id));
    }

    @Override
    @Transactional
    public MaintenanceCategory save(MaintenanceCategory category) {
        boolean duplicated = category.getId() == null
                ? categoryRepository.existsByName(category.getName())
                : categoryRepository.existsByNameAndIdNot(category.getName(), category.getId());
        if (duplicated) {
            throw new BusinessException("Hạng mục '" + category.getName() + "' đã tồn tại");
        }
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        MaintenanceCategory category = findById(id);
        if (requestRepository.countByCategoryId(id) > 0) {
            throw new BusinessException(
                    "Không thể xóa hạng mục đang được sử dụng bởi yêu cầu bảo trì. Hãy chuyển sang trạng thái ngừng sử dụng.");
        }
        categoryRepository.delete(category);
    }
}
