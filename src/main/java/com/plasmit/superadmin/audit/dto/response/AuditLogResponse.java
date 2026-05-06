package com.plasmit.superadmin.audit.dto.response;

public class AuditLogResponse {

    private Long id;
    private Long userId;
    private String userRole;
    private String action;
    private String entityType;
    private Long entityId;
    private String description;
    private String ipAddress;
    private String createdAt;

    public AuditLogResponse(Long id,
                            Long userId,
                            String userRole,
                            String action,
                            String entityType,
                            Long entityId,
                            String description,
                            String ipAddress,
                            String createdAt) {
        this.id = id;
        this.userId = userId;
        this.userRole = userRole;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.description = description;
        this.ipAddress = ipAddress;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getUserRole() { return userRole; }
    public String getAction() { return action; }
    public String getEntityType() { return entityType; }
    public Long getEntityId() { return entityId; }
    public String getDescription() { return description; }
    public String getIpAddress() { return ipAddress; }
    public String getCreatedAt() { return createdAt; }
}