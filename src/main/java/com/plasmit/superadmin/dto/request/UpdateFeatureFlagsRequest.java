package com.plasmit.superadmin.dto.request;

import java.util.List;

public class UpdateFeatureFlagsRequest {

    private List<FlagRequest> flags;

    public List<FlagRequest> getFlags() {
        return flags;
    }

    public void setFlags(List<FlagRequest> flags) {
        this.flags = flags;
    }

    public static class FlagRequest {
        private String key;
        private boolean enabled;

        public String getKey() {
            return key;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}