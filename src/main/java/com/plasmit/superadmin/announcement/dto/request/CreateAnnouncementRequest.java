package com.plasmit.superadmin.announcement.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class CreateAnnouncementRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String body;

    @NotBlank
    private String audience;

    private String scheduledAt;
    private List<Long> hospitalIds;

    public String getTitle() { return title; }
    public String getBody() { return body; }
    public String getAudience() { return audience; }
    public String getScheduledAt() { return scheduledAt; }
    public List<Long> getHospitalIds() { return hospitalIds; }

    public void setTitle(String title) { this.title = title; }
    public void setBody(String body) { this.body = body; }
    public void setAudience(String audience) { this.audience = audience; }
    public void setScheduledAt(String scheduledAt) { this.scheduledAt = scheduledAt; }
    public void setHospitalIds(List<Long> hospitalIds) { this.hospitalIds = hospitalIds; }
}