package com.plasmit.superadmin.announcement.service;

import com.plasmit.superadmin.announcement.dto.request.CreateAnnouncementRequest;
import com.plasmit.superadmin.announcement.dto.response.AnnouncementResponse;

import java.util.List;
import java.util.Map;

public interface AnnouncementService {

    List<AnnouncementResponse> getAnnouncements(String status, String audience);

    Map<String, Object> createAnnouncement(CreateAnnouncementRequest request);

    Map<String, Object> broadcastAnnouncement(Long announcementId);
}