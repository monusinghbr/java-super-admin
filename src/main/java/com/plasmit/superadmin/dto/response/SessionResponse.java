package com.plasmit.superadmin.dto.response;

public class SessionResponse {

    private Long id;
    private String device;
    private String browser;
    private String ipAddress;
    private String location;
    private String lastActiveAt;
    private boolean current;

    public SessionResponse(Long id, String device, String browser, String ipAddress,
                           String location, String lastActiveAt, boolean current) {
        this.id = id;
        this.device = device;
        this.browser = browser;
        this.ipAddress = ipAddress;
        this.location = location;
        this.lastActiveAt = lastActiveAt;
        this.current = current;
    }

    public Long getId() { return id; }
    public String getDevice() { return device; }
    public String getBrowser() { return browser; }
    public String getIpAddress() { return ipAddress; }
    public String getLocation() { return location; }
    public String getLastActiveAt() { return lastActiveAt; }
    public boolean isCurrent() { return current; }
}