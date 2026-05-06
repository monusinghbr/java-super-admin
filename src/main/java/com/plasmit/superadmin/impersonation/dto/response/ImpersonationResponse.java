package com.plasmit.superadmin.impersonation.dto.response;

public class ImpersonationResponse {

    private String token;
    private Long hospitalId;
    private Long tenantId;
    private String expiresAt;

    public ImpersonationResponse(String token, Long hospitalId,
                                 Long tenantId, String expiresAt) {
        this.token = token;
        this.hospitalId = hospitalId;
        this.tenantId = tenantId;
        this.expiresAt = expiresAt;
    }

    public String getToken() { return token; }
    public Long getHospitalId() { return hospitalId; }
    public Long getTenantId() { return tenantId; }
    public String getExpiresAt() { return expiresAt; }
}