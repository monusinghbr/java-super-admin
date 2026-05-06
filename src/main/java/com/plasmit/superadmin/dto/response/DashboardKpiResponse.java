package com.plasmit.superadmin.dto.response;

public class DashboardKpiResponse {

    private int totalHospitals;
    private int activeHospitals;
    private int inactiveHospitals;
    private int suspendedHospitals;
    private int activeSubscriptions;
    private int expiringPlansNext30Days;
    private int platformUsers;
    private int failedPaymentCount;

    public DashboardKpiResponse() {
    }

    public DashboardKpiResponse(int totalHospitals, int activeHospitals, int inactiveHospitals,
                                int suspendedHospitals, int activeSubscriptions,
                                int expiringPlansNext30Days, int platformUsers, int failedPaymentCount) {
        this.totalHospitals = totalHospitals;
        this.activeHospitals = activeHospitals;
        this.inactiveHospitals = inactiveHospitals;
        this.suspendedHospitals = suspendedHospitals;
        this.activeSubscriptions = activeSubscriptions;
        this.expiringPlansNext30Days = expiringPlansNext30Days;
        this.platformUsers = platformUsers;
        this.failedPaymentCount = failedPaymentCount;
    }

    public int getTotalHospitals() {
        return totalHospitals;
    }

    public void setTotalHospitals(int totalHospitals) {
        this.totalHospitals = totalHospitals;
    }

    public int getActiveHospitals() {
        return activeHospitals;
    }

    public void setActiveHospitals(int activeHospitals) {
        this.activeHospitals = activeHospitals;
    }

    public int getInactiveHospitals() {
        return inactiveHospitals;
    }

    public void setInactiveHospitals(int inactiveHospitals) {
        this.inactiveHospitals = inactiveHospitals;
    }

    public int getSuspendedHospitals() {
        return suspendedHospitals;
    }

    public void setSuspendedHospitals(int suspendedHospitals) {
        this.suspendedHospitals = suspendedHospitals;
    }

    public int getActiveSubscriptions() {
        return activeSubscriptions;
    }

    public void setActiveSubscriptions(int activeSubscriptions) {
        this.activeSubscriptions = activeSubscriptions;
    }

    public int getExpiringPlansNext30Days() {
        return expiringPlansNext30Days;
    }

    public void setExpiringPlansNext30Days(int expiringPlansNext30Days) {
        this.expiringPlansNext30Days = expiringPlansNext30Days;
    }

    public int getPlatformUsers() {
        return platformUsers;
    }

    public void setPlatformUsers(int platformUsers) {
        this.platformUsers = platformUsers;
    }

    public int getFailedPaymentCount() {
        return failedPaymentCount;
    }

    public void setFailedPaymentCount(int failedPaymentCount) {
        this.failedPaymentCount = failedPaymentCount;
    }
}