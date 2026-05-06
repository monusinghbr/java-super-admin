package com.plasmit.superadmin.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UpdateHospitalRequest {

    @NotBlank private String name;
    @NotBlank private String contactEmail;
    @NotBlank private String contactPhone;
    @NotBlank private String address;
    @NotBlank private String country;
    @NotBlank private String state;
    @NotBlank private String city;
    @NotBlank private String pincode;

    private String timezone;
    private Long subscriptionPlanId;

    public String getName() { return name; }
    public String getContactEmail() { return contactEmail; }
    public String getContactPhone() { return contactPhone; }
    public String getAddress() {
        return address;
    }
    public String getCountry() { return country; }
    public String getState() { return state; }
    public String getCity() { return city; }
    public String getPincode() { return pincode; }
    public String getTimezone() { return timezone; }
    public Long getSubscriptionPlanId() { return subscriptionPlanId; }

    public void setName(String name) { this.name = name; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setCountry(String country) { this.country = country; }
    public void setState(String state) { this.state = state; }
    public void setCity(String city) { this.city = city; }
    public void setPincode(String pincode) { this.pincode = pincode; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    public void setSubscriptionPlanId(Long subscriptionPlanId) { this.subscriptionPlanId = subscriptionPlanId; }
}