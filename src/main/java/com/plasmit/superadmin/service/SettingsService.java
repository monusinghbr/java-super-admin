package com.plasmit.superadmin.service;

import com.plasmit.superadmin.dto.request.UpdateFeatureFlagsRequest;
import com.plasmit.superadmin.dto.request.UpdateSettingsRequest;
import com.plasmit.superadmin.dto.response.FeatureFlagResponse;
import com.plasmit.superadmin.dto.response.SettingsResponse;

import java.util.List;
import java.util.Map;

public interface SettingsService {

    SettingsResponse getSettings();

    Map<String, Object> updateSettings(UpdateSettingsRequest request);

    List<FeatureFlagResponse> getFeatureFlags();

    Map<String, Object> updateFeatureFlags(UpdateFeatureFlagsRequest request);
}