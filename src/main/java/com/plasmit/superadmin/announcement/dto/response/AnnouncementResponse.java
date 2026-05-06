package com.plasmit.superadmin.announcement.dto.response;

public class AnnouncementResponse {

    private Long id;
    private String title;
    private String body;
    private String audience;
    private String status;
    private String scheduledAt;
    private String broadcastedAt;
    private String createdAt;

    public AnnouncementResponse(Long id, String title, String body, String audience,
                                String status, String scheduledAt, String broadcastedAt,
                                String createdAt) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.audience = audience;
        this.status = status;
        this.scheduledAt = scheduledAt;
        this.broadcastedAt = broadcastedAt;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public String getAudience() { return audience; }
    public String getStatus() { return status; }
    public String getScheduledAt() { return scheduledAt; }
    public String getBroadcastedAt() { return broadcastedAt; }
    public String getCreatedAt() { return createdAt; }
}