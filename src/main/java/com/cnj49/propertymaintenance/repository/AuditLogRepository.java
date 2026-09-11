package com.cnj49.propertymaintenance.repository;

import com.cnj49.propertymaintenance.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    /** Timeline cua mot doi tuong nghiep vu, cu nhat truoc. */
    List<AuditLog> findByEntityTypeAndEntityIdOrderByCreatedAtAsc(String entityType, Long entityId);

    Page<AuditLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
