package com.plasmit.superadmin.support.service;

import com.plasmit.superadmin.support.dto.response.SupportTicketResponse;

import java.util.List;

public interface SupportTicketService {

    List<SupportTicketResponse> getTickets(String status,
                                           Long hospitalId);

    void updateStatus(Long ticketId,
                      String status);

    void assignTicket(Long ticketId,
                      Long ownerId,
                      String ownerName);
}