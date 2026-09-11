package com.cnj49.propertymaintenance.service;

import com.cnj49.propertymaintenance.dto.MaintenanceRequestForm;
import com.cnj49.propertymaintenance.entity.MaintenanceRequest;
import com.cnj49.propertymaintenance.enums.Priority;
import com.cnj49.propertymaintenance.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/** Quan ly yeu cau bao tri - nghiep vu trung tam. */
public interface MaintenanceRequestService {

    Page<MaintenanceRequest> search(String keyword, Long propertyId, Long categoryId,
                                    Priority priority, RequestStatus status,
                                    LocalDate fromDate, LocalDate toDate, Pageable pageable);

    MaintenanceRequest findById(Long id);

    MaintenanceRequestForm toForm(MaintenanceRequest request);

    /** Tao yeu cau moi, luon o trang thai NEW va tu sinh ma MR-yyyy-xxxx. */
    MaintenanceRequest create(MaintenanceRequestForm form);

    MaintenanceRequest update(Long id, MaintenanceRequestForm form);

    /** Chuyen trang thai thu cong (vi du NEW -> UNDER_REVIEW -> WAITING_QUOTATION). */
    MaintenanceRequest changeStatus(Long id, RequestStatus newStatus);

    /** BR05: chi dong duoc yeu cau da COMPLETED (tuc da nghiem thu dat). */
    MaintenanceRequest close(Long id);

    MaintenanceRequest cancel(Long id);

    void delete(Long id);

    List<MaintenanceRequest> findByProperty(Long propertyId);

    List<MaintenanceRequest> findRecent(int limit);

    List<MaintenanceRequest> findOverdue();
}
