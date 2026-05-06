package com.plasmit.superadmin.announcement.service.impl;

import com.plasmit.superadmin.announcement.dto.request.CreateAnnouncementRequest;
import com.plasmit.superadmin.announcement.dto.response.AnnouncementResponse;
import com.plasmit.superadmin.announcement.repository.AnnouncementRepository;
import com.plasmit.superadmin.announcement.service.AnnouncementService;
import com.plasmit.superadmin.security.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    private static final Logger log = LoggerFactory.getLogger(AnnouncementServiceImpl.class);

    private final AnnouncementRepository announcementRepository;

    public AnnouncementServiceImpl(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @Override
    public List<AnnouncementResponse> getAnnouncements(String status, String audience) {
        log.info("Fetching announcements. status={}, audience={}, userId={}, role={}",
                status, audience, TenantContext.getUserId(), TenantContext.getRole());

        return announcementRepository.findAnnouncements(status, audience);
    }

    @Override
    @Transactional
    public Map<String, Object> createAnnouncement(CreateAnnouncementRequest request) {
        Long userId = TenantContext.getUserId();

        log.info("Creating announcement. title={}, audience={}, createdBy={}",
                request.getTitle(), request.getAudience(), userId);

        validateCreateRequest(request);

        Long announcementId = announcementRepository.createAnnouncement(request, userId);

        if ("SPECIFIC_HOSPITALS".equalsIgnoreCase(request.getAudience())) {
            announcementRepository.addHospitalTargets(announcementId, request.getHospitalIds());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", announcementId);

        log.info("Announcement created successfully. id={}, audience={}",
                announcementId, request.getAudience());

        return response;
    }

    @Override
    public Map<String, Object> broadcastAnnouncement(Long announcementId) {
        Long userId = TenantContext.getUserId();

        log.info("Broadcasting announcement. announcementId={}, userId={}",
                announcementId, userId);

        if (!announcementRepository.existsById(announcementId)) {
            throw new IllegalArgumentException("Announcement not found");
        }

        int updated = announcementRepository.broadcastAnnouncement(announcementId, userId);

        if (updated == 0) {
            throw new IllegalArgumentException("Announcement cannot be broadcasted from current status");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", announcementId);
        response.put("status", "BROADCASTING");
        response.put("broadcastStartedAt", LocalDateTime.now());

        log.info("Announcement broadcast started. announcementId={}", announcementId);

        return response;
    }

    private void validateCreateRequest(CreateAnnouncementRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Announcement request is required");
        }

        if (isBlank(request.getTitle())) {
            throw new IllegalArgumentException("Title is required");
        }

        if (isBlank(request.getBody())) {
            throw new IllegalArgumentException("Body is required");
        }

        if (isBlank(request.getAudience())) {
            throw new IllegalArgumentException("Audience is required");
        }

        String audience = request.getAudience();

        if (!"ALL_HOSPITALS".equalsIgnoreCase(audience)
                && !"SPECIFIC_HOSPITALS".equalsIgnoreCase(audience)
                && !"SUPER_ADMINS".equalsIgnoreCase(audience)) {
            throw new IllegalArgumentException("Invalid announcement audience");
        }

        if ("SPECIFIC_HOSPITALS".equalsIgnoreCase(audience)
                && (request.getHospitalIds() == null || request.getHospitalIds().isEmpty())) {
            throw new IllegalArgumentException("Hospital ids are required for specific hospital audience");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}