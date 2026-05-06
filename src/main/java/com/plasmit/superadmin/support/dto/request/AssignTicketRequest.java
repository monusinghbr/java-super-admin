package com.plasmit.superadmin.support.dto.request;

import jakarta.validation.constraints.NotNull;

public class AssignTicketRequest {

    @NotNull
    private Long ownerUserId;

    private String ownerName;

    public Long getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(Long ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}