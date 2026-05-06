package com.plasmit.superadmin.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plasmit.superadmin.dto.request.UpdateFeatureFlagsRequest;
import com.plasmit.superadmin.dto.request.UpdateSettingsRequest;
import com.plasmit.superadmin.dto.response.FeatureFlagResponse;
import com.plasmit.superadmin.dto.response.SettingsResponse;
import com.plasmit.superadmin.repository.SettingsRepository;
import com.plasmit.superadmin.security.TenantContext;
import com.plasmit.superadmin.service.SettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SettingsServiceImpl implements SettingsService {

    private static final Logger log = LoggerFactory.getLogger(SettingsServiceImpl.class);

    private final SettingsRepository settingsRepository;
    private final ObjectMapper objectMapper;

    public SettingsServiceImpl(SettingsRepository settingsRepository, ObjectMapper objectMapper) {
        this.settingsRepository = settingsRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public SettingsResponse getSettings() {
        log.info("Fetching project settings. userId={}, role={}",
                TenantContext.getUserId(), TenantContext.getRole());

        try {
            String json = settingsRepository.findProjectSettingsJson();
            JsonNode node = objectMapper.readTree(json);
            return new SettingsResponse(node);
        } catch (JsonProcessingException ex) {
            log.error("Invalid settings JSON", ex);
            throw new IllegalStateException("Invalid settings configuration");
        }
    }

    @Override
    public Map<String, Object> updateSettings(UpdateSettingsRequest request) {
        Long userId = TenantContext.getUserId();

        log.info("Updating project settings. userId={}, role={}",
                userId, TenantContext.getRole());

        validateSettings(request);

        try {
            String jsonValue = objectMapper.writeValueAsString(request);
            int updated = settingsRepository.updateProjectSettings(jsonValue, userId);

            if (updated == 0) {
                throw new IllegalStateException("Project settings record not found");
            }

            Map<String, Object> response = new HashMap<>();
            response.put("updatedAt", LocalDateTime.now());
            response.put("updatedBy", userId);
            return response;

        } catch (JsonProcessingException ex) {
            log.error("Failed to serialize settings request", ex);
            throw new IllegalArgumentException("Invalid settings payload");
        }
    }

    @Override
    public List<FeatureFlagResponse> getFeatureFlags() {
        log.info("Fetching feature flags. userId={}", TenantContext.getUserId());
        return settingsRepository.findFeatureFlags();
    }

    @Override
    public Map<String, Object> updateFeatureFlags(UpdateFeatureFlagsRequest request) {
        Long userId = TenantContext.getUserId();

        log.info("Updating feature flags. userId={}, totalFlags={}",
                userId, request.getFlags() == null ? 0 : request.getFlags().size());

        if (request.getFlags() == null || request.getFlags().isEmpty()) {
            throw new IllegalArgumentException("Feature flags are required");
        }

        for (UpdateFeatureFlagsRequest.FlagRequest flag : request.getFlags()) {
            if (flag.getKey() == null || flag.getKey().isBlank()) {
                throw new IllegalArgumentException("Feature flag key is required");
            }

            settingsRepository.updateFeatureFlag(flag.getKey(), flag.isEnabled(), userId);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("updatedAt", LocalDateTime.now());
        response.put("updatedBy", userId);
        return response;
    }

    private void validateSettings(UpdateSettingsRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Settings payload is required");
        }

        if (request.getGeneral() == null) {
            throw new IllegalArgumentException("General settings are required");
        }

        if (request.getBranding() == null) {
            throw new IllegalArgumentException("Branding settings are required");
        }

        if (request.getCommunication() == null) {
            throw new IllegalArgumentException("Communication settings are required");
        }

        if (request.getSecurity() == null) {
            throw new IllegalArgumentException("Security settings are required");
        }

        if (request.getIntegrations() == null) {
            throw new IllegalArgumentException("Integration settings are required");
        }

        if (isBlank(request.getGeneral().getApplicationName())) {
            throw new IllegalArgumentException("Application name is required");
        }

        if (isBlank(request.getGeneral().getDefaultTimezone())) {
            throw new IllegalArgumentException("Default timezone is required");
        }

        if (isBlank(request.getBranding().getSupportEmail())) {
            throw new IllegalArgumentException("Support email is required");
        }

        if (request.getSecurity().getAuditRetentionDays() == null
                || request.getSecurity().getAuditRetentionDays() <= 0) {
            throw new IllegalArgumentException("Audit retention days must be positive");
        }

        if (request.getSecurity().getImpersonationTimeoutMinutes() == null
                || request.getSecurity().getImpersonationTimeoutMinutes() <= 0) {
            throw new IllegalArgumentException("Impersonation timeout must be positive");
        }

        if (request.getIntegrations().getUploadSizeLimitMb() == null
                || request.getIntegrations().getUploadSizeLimitMb() <= 0) {
            throw new IllegalArgumentException("Upload size limit must be positive");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}