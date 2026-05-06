package com.plasmit.superadmin.dto.response;

public class FeatureFlagResponse {

    private String key;
    private String name;
    private boolean enabled;

    public FeatureFlagResponse(String key, String name, boolean enabled) {
        this.key = key;
        this.name = name;
        this.enabled = enabled;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enabled;
    }
}