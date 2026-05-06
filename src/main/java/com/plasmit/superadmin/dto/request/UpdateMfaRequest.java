package com.plasmit.superadmin.dto.request;

public class UpdateMfaRequest {

    private boolean enabled;
    private String method;

    public boolean isEnabled() { return enabled; }
    public String getMethod() { return method; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setMethod(String method) { this.method = method; }
}