package com.plasmit.superadmin.support.service.impl;

import com.plasmit.superadmin.security.TenantContext;
import com.plasmit.superadmin.support.dto.response.SupportTicketResponse;
import com.plasmit.superadmin.support.repository.SupportTicketRepository;
import com.plasmit.superadmin.support.service.SupportTicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportTicketServiceImpl implements SupportTicketService {

    private static final Logger log =
            LoggerFactory.getLogger(SupportTicketServiceImpl.class);

    private final SupportTicketRepository repo;

    public SupportTicketServiceImpl(SupportTicketRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<SupportTicketResponse> getTickets(String status,
                                                  Long hospitalId) {

        log.info("Fetching support tickets status={}, hospitalId={}",
                status,
                hospitalId);

        return repo.findTickets(status, hospitalId);
    }

    @Override
    public void updateStatus(Long ticketId,
                             String status) {

        Long userId = TenantContext.getUserId();

        log.info("Updating ticket status ticketId={}, status={}",
                ticketId,
                status);

        repo.updateStatus(ticketId, status, userId);
    }

    @Override
    public void assignTicket(Long ticketId,
                              Long ownerId,
                              String ownerName) {

        log.info("Assigning ticket ticketId={}, ownerId={}",
                ticketId,
                ownerId);

        repo.assignTicket(ticketId, ownerId, ownerName);
    }
}