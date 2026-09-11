package com.cnj49.propertymaintenance.service;

import com.cnj49.propertymaintenance.dto.WorkOrderForm;
import com.cnj49.propertymaintenance.entity.WorkOrder;
import com.cnj49.propertymaintenance.enums.WorkOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/** Quan ly phieu cong viec va tien do thuc hien. */
public interface WorkOrderService {

    Page<WorkOrder> search(String keyword, Long contractorId, WorkOrderStatus status, Pageable pageable);

    WorkOrder findById(Long id);

    List<WorkOrder> findByRequest(Long requestId);

    List<WorkOrder> findByContractor(Long contractorId);

    /** Danh sach cong viec da hoan thanh, dang cho nghiem thu. */
    List<WorkOrder> findAwaitingInspection();

    /**
     * BR01: chi tao duoc khi yeu cau da co bao gia APPROVED.
     * BR09: nha thau lay tu bao gia da duyet.
     * BR06: ngay du kien hoan thanh khong duoc truoc ngay bat dau.
     */
    WorkOrder create(WorkOrderForm form);

    /** BR10: bat dau cong viec -> yeu cau chuyen IN_PROGRESS. */
    WorkOrder start(Long id);

    WorkOrder pause(Long id);

    WorkOrder resume(Long id);

    /** BR11: hoan thanh cong viec -> yeu cau chuyen WAITING_INSPECTION. */
    WorkOrder complete(Long id, String result);

    WorkOrder cancel(Long id);

    long countActive();
}
