package com.plasmit.superadmin.support.dto.response;

public class SupportTicketResponse {

    private Long id;
    private String ticketCode;
    private Long hospitalId;
    private String subject;
    private String priority;
    private String status;
    private String ownerName;
    private String createdAt;

    public SupportTicketResponse(Long id,
                                 String ticketCode,
                                 Long hospitalId,
                                 String subject,
                                 String priority,
                                 String status,
                                 String ownerName,
                                 String createdAt) {

        this.id = id;
        this.ticketCode = ticketCode;
        this.hospitalId = hospitalId;
        this.subject = subject;
        this.priority = priority;
        this.status = status;
        this.ownerName = ownerName;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getTicketCode() { return ticketCode; }
    public Long getHospitalId() { return hospitalId; }
    public String getSubject() { return subject; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public String getOwnerName() { return ownerName; }
    public String getCreatedAt() { return createdAt; }
}