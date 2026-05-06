package com.plasmit.superadmin.audit.service.impl;

import com.plasmit.superadmin.audit.dto.response.AuditLogResponse;
import com.plasmit.superadmin.audit.repository.AuditLogRepository;
import com.plasmit.superadmin.audit.service.AuditLogService;
import com.plasmit.superadmin.security.TenantContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository repository;

    public AuditLogServiceImpl(AuditLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public void log(String action,
                    String entityType,
                    Long entityId,
                    String description,
                    String ipAddress) {

        repository.save(
                TenantContext.getUserId(),
                TenantContext.getRole(),
                action,
                entityType,
                entityId,
                description,
                ipAddress
        );
    }

    @Override
    public List<AuditLogResponse> getLogs(String action,
                                          String entityType,
                                          Long userId) {

        return repository.findLogs(action, entityType, userId);
    }
}