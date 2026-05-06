package com.plasmit.superadmin.dto.response;

public class ProfileResponse {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String avatarUrl;
    private boolean mfaEnabled;
    private String lastLoginAt;

    public ProfileResponse(Long id, String name, String email, String role,
                           String avatarUrl, boolean mfaEnabled, String lastLoginAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.avatarUrl = avatarUrl;
        this.mfaEnabled = mfaEnabled;
        this.lastLoginAt = lastLoginAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getAvatarUrl() { return avatarUrl; }
    public boolean isMfaEnabled() { return mfaEnabled; }
    public String getLastLoginAt() { return lastLoginAt; }
}