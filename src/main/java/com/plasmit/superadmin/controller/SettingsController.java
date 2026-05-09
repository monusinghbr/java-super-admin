package com.plasmit.superadmin.controller;

import com.plasmit.superadmin.common.ApiResponse;
import com.plasmit.superadmin.dto.request.UpdateFeatureFlagsRequest;
import com.plasmit.superadmin.dto.request.UpdateSettingsRequest;
import com.plasmit.superadmin.dto.response.FeatureFlagResponse;
import com.plasmit.superadmin.dto.response.SettingsResponse;
import com.plasmit.superadmin.service.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/super-admin/settings")
public class SettingsController {

    private static final Logger log = LoggerFactory.getLogger(SettingsController.class);

    private final SettingsService settingsService;

    public SettingsController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping
    public ApiResponse<SettingsResponse> getSettings() {
        log.info("Get settings API called");
        return ApiResponse.success("Settings fetched", settingsService.getSettings());
    }

    @PutMapping
    public ApiResponse<Map<String, Object>> updateSettings(@RequestBody UpdateSettingsRequest request) {
        log.info("Update settings API called");
        return ApiResponse.success("Settings updated successfully", settingsService.updateSettings(request));
    }

    @GetMapping("/feature-flags")
    public ApiResponse<List<FeatureFlagResponse>> getFeatureFlags() {
        log.info("Get feature flags API called");
        return ApiResponse.success("Feature flags fetched", settingsService.getFeatureFlags());
    }

    @PutMapping("/feature-flags")
    public ApiResponse<Map<String, Object>> updateFeatureFlags(@RequestBody UpdateFeatureFlagsRequest request) {
        log.info("Update feature flags API called");
        return ApiResponse.success("Feature flags updated successfully", settingsService.updateFeatureFlags(request));
    }
}
