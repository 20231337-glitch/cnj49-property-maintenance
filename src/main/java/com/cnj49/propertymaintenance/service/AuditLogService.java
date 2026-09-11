package com.cnj49.propertymaintenance.service;

import com.cnj49.propertymaintenance.entity.AuditLog;
import com.cnj49.propertymaintenance.enums.AuditAction;

import java.util.List;

/** Ghi va doc nhat ky hanh dong nghiep vu (dung cho timeline). */
public interface AuditLogService {

    void log(AuditAction action, String entityType, Long entityId, String description);

    List<AuditLog> findTimeline(String entityType, Long entityId);
}
