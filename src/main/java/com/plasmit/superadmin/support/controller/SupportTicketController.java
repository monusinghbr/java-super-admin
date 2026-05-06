package com.plasmit.superadmin.support.controller;

import com.plasmit.superadmin.common.ApiResponse;
import com.plasmit.superadmin.support.dto.request.AssignTicketRequest;
import com.plasmit.superadmin.support.dto.request.UpdateTicketStatusRequest;
import com.plasmit.superadmin.support.dto.response.SupportTicketResponse;
import com.plasmit.superadmin.support.service.SupportTicketService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/super-admin/support-tickets")
@CrossOrigin("*")
public class SupportTicketController {

    private final SupportTicketService service;

    public SupportTicketController(SupportTicketService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<List<SupportTicketResponse>> getTickets(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long hospitalId) {

        return ApiResponse.success(
                "Support tickets fetched",
                service.getTickets(status, hospitalId)
        );
    }

    @PatchMapping("/{ticketId}/status")
    public ApiResponse<?> updateStatus(
            @PathVariable Long ticketId,
            @Valid @RequestBody UpdateTicketStatusRequest request) {

        service.updateStatus(ticketId, request.getStatus());

        return ApiResponse.success(
                "Ticket status updated",
                null
        );
    }

    @PatchMapping("/{ticketId}/assign")
    public ApiResponse<?> assignTicket(
            @PathVariable Long ticketId,
            @Valid @RequestBody AssignTicketRequest request) {

        service.assignTicket(
                ticketId,
                request.getOwnerUserId(),
                request.getOwnerName()
        );

        return ApiResponse.success(
                "Ticket assigned successfully",
                null
        );
    }
}