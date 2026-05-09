package com.plasmit.superadmin.announcement.controller;

import com.plasmit.superadmin.announcement.dto.request.CreateAnnouncementRequest;
import com.plasmit.superadmin.announcement.dto.response.AnnouncementResponse;
import com.plasmit.superadmin.announcement.service.AnnouncementService;
import com.plasmit.superadmin.common.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/super-admin/announcements")
public class AnnouncementController {

    private static final Logger log = LoggerFactory.getLogger(AnnouncementController.class);

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    public ApiResponse<List<AnnouncementResponse>> getAnnouncements(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String audience
    ) {
        log.info("Get announcements API called. status={}, audience={}", status, audience);

        return ApiResponse.success(
                "Announcements fetched",
                announcementService.getAnnouncements(status, audience)
        );
    }

    @PostMapping
    public ApiResponse<Map<String, Object>> createAnnouncement(
            @Valid @RequestBody CreateAnnouncementRequest request
    ) {
        log.info("Create announcement API called. title={}, audience={}",
                request.getTitle(), request.getAudience());

        return ApiResponse.success(
                "Announcement created successfully",
                announcementService.createAnnouncement(request)
        );
    }

    @PostMapping("/{announcementId}/broadcast")
    public ApiResponse<Map<String, Object>> broadcastAnnouncement(
            @PathVariable Long announcementId
    ) {
        log.info("Broadcast announcement API called. announcementId={}", announcementId);

        return ApiResponse.success(
                "Announcement broadcast started",
                announcementService.broadcastAnnouncement(announcementId)
        );
    }
}
