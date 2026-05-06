package com.plasmit.superadmin.dto.response;

import java.util.List;

public class DashboardSummaryResponse {

    private List<CriticalAlert> criticalAlerts;
    private SecurityPosture securityPosture;
    private List<OnboardingQueueItem> onboardingQueue;

    public DashboardSummaryResponse() {
    }

    public DashboardSummaryResponse(List<CriticalAlert> criticalAlerts,
                                    SecurityPosture securityPosture,
                                    List<OnboardingQueueItem> onboardingQueue) {
        this.criticalAlerts = criticalAlerts;
        this.securityPosture = securityPosture;
        this.onboardingQueue = onboardingQueue;
    }

    public List<CriticalAlert> getCriticalAlerts() {
        return criticalAlerts;
    }

    public void setCriticalAlerts(List<CriticalAlert> criticalAlerts) {
        this.criticalAlerts = criticalAlerts;
    }

    public SecurityPosture getSecurityPosture() {
        return securityPosture;
    }

    public void setSecurityPosture(SecurityPosture securityPosture) {
        this.securityPosture = securityPosture;
    }

    public List<OnboardingQueueItem> getOnboardingQueue() {
        return onboardingQueue;
    }

    public void setOnboardingQueue(List<OnboardingQueueItem> onboardingQueue) {
        this.onboardingQueue = onboardingQueue;
    }

    public static class CriticalAlert {
        private String id;
        private String title;
        private String message;
        private String category;
        private String severity;

        public CriticalAlert() {
        }

        public CriticalAlert(String id, String title, String message, String category, String severity) {
            this.id = id;
            this.title = title;
            this.message = message;
            this.category = category;
            this.severity = severity;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getMessage() {
            return message;
        }

        public String getCategory() {
            return category;
        }

        public String getSeverity() {
            return severity;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setSeverity(String severity) {
            this.severity = severity;
        }
    }

    public static class SecurityPosture {
        private int mfaEnabledPercentage;
        private int rbacReviewedPercentage;
        private int auditRetentionHealthyPercentage;

        public SecurityPosture() {
        }

        public SecurityPosture(int mfaEnabledPercentage, int rbacReviewedPercentage, int auditRetentionHealthyPercentage) {
            this.mfaEnabledPercentage = mfaEnabledPercentage;
            this.rbacReviewedPercentage = rbacReviewedPercentage;
            this.auditRetentionHealthyPercentage = auditRetentionHealthyPercentage;
        }

        public int getMfaEnabledPercentage() {
            return mfaEnabledPercentage;
        }

        public int getRbacReviewedPercentage() {
            return rbacReviewedPercentage;
        }

        public int getAuditRetentionHealthyPercentage() {
            return auditRetentionHealthyPercentage;
        }

        public void setMfaEnabledPercentage(int mfaEnabledPercentage) {
            this.mfaEnabledPercentage = mfaEnabledPercentage;
        }

        public void setRbacReviewedPercentage(int rbacReviewedPercentage) {
            this.rbacReviewedPercentage = rbacReviewedPercentage;
        }

        public void setAuditRetentionHealthyPercentage(int auditRetentionHealthyPercentage) {
            this.auditRetentionHealthyPercentage = auditRetentionHealthyPercentage;
        }
    }

    public static class OnboardingQueueItem {
        private String hospitalId;
        private String hospitalName;
        private String hospitalCode;
        private String currentStep;
        private String owner;

        public OnboardingQueueItem() {
        }

        public OnboardingQueueItem(String hospitalId, String hospitalName, String hospitalCode,
                                   String currentStep, String owner) {
            this.hospitalId = hospitalId;
            this.hospitalName = hospitalName;
            this.hospitalCode = hospitalCode;
            this.currentStep = currentStep;
            this.owner = owner;
        }

        public String getHospitalId() {
            return hospitalId;
        }

        public String getHospitalName() {
            return hospitalName;
        }

        public String getHospitalCode() {
            return hospitalCode;
        }

        public String getCurrentStep() {
            return currentStep;
        }

        public String getOwner() {
            return owner;
        }

        public void setHospitalId(String hospitalId) {
            this.hospitalId = hospitalId;
        }

        public void setHospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
        }

        public void setHospitalCode(String hospitalCode) {
            this.hospitalCode = hospitalCode;
        }

        public void setCurrentStep(String currentStep) {
            this.currentStep = currentStep;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }
    }
}