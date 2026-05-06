package com.plasmit.superadmin.dto.response;

import com.fasterxml.jackson.databind.JsonNode;

public class SettingsResponse {

    private JsonNode settings;

    public SettingsResponse(JsonNode settings) {
        this.settings = settings;
    }

    public JsonNode getSettings() {
        return settings;
    }

    public void setSettings(JsonNode settings) {
        this.settings = settings;
    }
}