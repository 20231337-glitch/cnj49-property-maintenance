package com.cnj49.propertymaintenance.service;

import com.cnj49.propertymaintenance.dto.InspectionForm;
import com.cnj49.propertymaintenance.entity.Inspection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/** Quan ly nghiem thu cong viec. */
public interface InspectionService {

    Page<Inspection> findAll(Pageable pageable);

    List<Inspection> findByWorkOrder(Long workOrderId);

    Inspection findById(Long id);

    /**
     * BR04: chi nghiem thu duoc WorkOrder da COMPLETED.
     * BR12 PASSED: WorkOrder -> ACCEPTED, Request -> COMPLETED, tu dong ghi chi phi thuc te.
     * BR13 FAILED: WorkOrder -> IN_PROGRESS, Request -> IN_PROGRESS.
     */
    Inspection inspect(InspectionForm form);
}
