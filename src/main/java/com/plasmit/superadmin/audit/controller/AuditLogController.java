package com.plasmit.superadmin.audit.controller;

import com.plasmit.superadmin.audit.dto.response.AuditLogResponse;
import com.plasmit.superadmin.audit.service.AuditLogService;
import com.plasmit.superadmin.common.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/super-admin/audit-logs")
public class AuditLogController {

    private final AuditLogService service;

    public AuditLogController(AuditLogService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<List<AuditLogResponse>> getLogs(
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) Long userId) {

        return ApiResponse.success(
                "Audit logs fetched successfully",
                service.getLogs(action, entityType, userId)
        );
    }
}
