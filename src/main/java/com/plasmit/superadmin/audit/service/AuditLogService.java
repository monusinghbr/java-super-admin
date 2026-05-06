package com.plasmit.superadmin.audit.service;

import com.plasmit.superadmin.audit.dto.response.AuditLogResponse;

import java.util.List;

public interface AuditLogService {

    void log(String action,
             String entityType,
             Long entityId,
             String description,
             String ipAddress);

    List<AuditLogResponse> getLogs(String action,
                                  String entityType,
                                  Long userId);
}