package com.plasmit.superadmin.dto.response;

public class HospitalResponse {

    private Long id;
    private Long tenantId;
    private String name;
    private String code;
    private String contactEmail;
    private String contactPhone;
    private String country;
    private String state;
    private String city;
    private String pincode;
    private String timezone;
    private Long subscriptionPlanId;
    private String status;
    private String createdAt;

    public HospitalResponse(Long id, Long tenantId, String name, String code,
                            String contactEmail, String contactPhone,
                            String country, String state, String city,
                            String pincode, String timezone, Long subscriptionPlanId,
                            String status, String createdAt) {
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.code = code;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.country = country;
        this.state = state;
        this.city = city;
        this.pincode = pincode;
        this.timezone = timezone;
        this.subscriptionPlanId = subscriptionPlanId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public Long getTenantId() { return tenantId; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public String getContactEmail() { return contactEmail; }
    public String getContactPhone() { return contactPhone; }
    public String getCountry() { return country; }
    public String getState() { return state; }
    public String getCity() { return city; }
    public String getPincode() { return pincode; }
    public String getTimezone() { return timezone; }
    public Long getSubscriptionPlanId() { return subscriptionPlanId; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
}