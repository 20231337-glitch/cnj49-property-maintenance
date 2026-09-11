package com.cnj49.propertymaintenance.service.impl;

import com.cnj49.propertymaintenance.entity.AuditLog;
import com.cnj49.propertymaintenance.enums.AuditAction;
import com.cnj49.propertymaintenance.repository.AuditLogRepository;
import com.cnj49.propertymaintenance.service.AuditLogService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    @Transactional
    public void log(AuditAction action, String entityType, Long entityId, String description) {
        auditLogRepository.save(new AuditLog(action, entityType, entityId, description, currentUsername()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditLog> findTimeline(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityIdOrderByCreatedAtAsc(entityType, entityId);
    }

    /** Lay ten dang nhap hien tai, tra ve "system" khi chay nen (vi du seed data). */
    private String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return "system";
        }
        return auth.getName();
    }
}
