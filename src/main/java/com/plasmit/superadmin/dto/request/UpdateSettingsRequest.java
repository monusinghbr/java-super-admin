package com.plasmit.superadmin.dto.request;

import java.util.List;

public class UpdateSettingsRequest {

    private General general;
    private Branding branding;
    private Communication communication;
    private Security security;
    private Integrations integrations;

    public General getGeneral() { return general; }
    public Branding getBranding() { return branding; }
    public Communication getCommunication() { return communication; }
    public Security getSecurity() { return security; }
    public Integrations getIntegrations() { return integrations; }

    public void setGeneral(General general) { this.general = general; }
    public void setBranding(Branding branding) { this.branding = branding; }
    public void setCommunication(Communication communication) { this.communication = communication; }
    public void setSecurity(Security security) { this.security = security; }
    public void setIntegrations(Integrations integrations) { this.integrations = integrations; }

    public static class General {
        private String applicationName;
        private String defaultTimezone;
        private String dateFormat;
        private String defaultCurrency;

        public String getApplicationName() { return applicationName; }
        public String getDefaultTimezone() { return defaultTimezone; }
        public String getDateFormat() { return dateFormat; }
        public String getDefaultCurrency() { return defaultCurrency; }

        public void setApplicationName(String applicationName) { this.applicationName = applicationName; }
        public void setDefaultTimezone(String defaultTimezone) { this.defaultTimezone = defaultTimezone; }
        public void setDateFormat(String dateFormat) { this.dateFormat = dateFormat; }
        public void setDefaultCurrency(String defaultCurrency) { this.defaultCurrency = defaultCurrency; }
    }

    public static class Branding {
        private String brandDisplayName;
        private String primaryColor;
        private String logoUrl;
        private String supportEmail;

        public String getBrandDisplayName() { return brandDisplayName; }
        public String getPrimaryColor() { return primaryColor; }
        public String getLogoUrl() { return logoUrl; }
        public String getSupportEmail() { return supportEmail; }

        public void setBrandDisplayName(String brandDisplayName) { this.brandDisplayName = brandDisplayName; }
        public void setPrimaryColor(String primaryColor) { this.primaryColor = primaryColor; }
        public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
        public void setSupportEmail(String supportEmail) { this.supportEmail = supportEmail; }
    }

    public static class Communication {
        private String emailProvider;
        private String emailProviderKey;
        private String smsProvider;
        private String smsProviderKey;

        public String getEmailProvider() { return emailProvider; }
        public String getEmailProviderKey() { return emailProviderKey; }
        public String getSmsProvider() { return smsProvider; }
        public String getSmsProviderKey() { return smsProviderKey; }

        public void setEmailProvider(String emailProvider) { this.emailProvider = emailProvider; }
        public void setEmailProviderKey(String emailProviderKey) { this.emailProviderKey = emailProviderKey; }
        public void setSmsProvider(String smsProvider) { this.smsProvider = smsProvider; }
        public void setSmsProviderKey(String smsProviderKey) { this.smsProviderKey = smsProviderKey; }
    }

    public static class Security {
        private Integer auditRetentionDays;
        private Integer impersonationTimeoutMinutes;
        private Boolean requireMfaForImpersonation;

        public Integer getAuditRetentionDays() { return auditRetentionDays; }
        public Integer getImpersonationTimeoutMinutes() { return impersonationTimeoutMinutes; }
        public Boolean getRequireMfaForImpersonation() { return requireMfaForImpersonation; }

        public void setAuditRetentionDays(Integer auditRetentionDays) { this.auditRetentionDays = auditRetentionDays; }
        public void setImpersonationTimeoutMinutes(Integer impersonationTimeoutMinutes) { this.impersonationTimeoutMinutes = impersonationTimeoutMinutes; }
        public void setRequireMfaForImpersonation(Boolean requireMfaForImpersonation) { this.requireMfaForImpersonation = requireMfaForImpersonation; }
    }

    public static class Integrations {
        private String storageProvider;
        private String bucketName;
        private Integer uploadSizeLimitMb;
        private List<String> allowedFileTypes;

        public String getStorageProvider() { return storageProvider; }
        public String getBucketName() { return bucketName; }
        public Integer getUploadSizeLimitMb() { return uploadSizeLimitMb; }
        public List<String> getAllowedFileTypes() { return allowedFileTypes; }

        public void setStorageProvider(String storageProvider) { this.storageProvider = storageProvider; }
        public void setBucketName(String bucketName) { this.bucketName = bucketName; }
        public void setUploadSizeLimitMb(Integer uploadSizeLimitMb) { this.uploadSizeLimitMb = uploadSizeLimitMb; }
        public void setAllowedFileTypes(List<String> allowedFileTypes) { this.allowedFileTypes = allowedFileTypes; }
    }
}