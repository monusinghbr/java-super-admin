package com.plasmit.superadmin.impersonation.controller;

import com.plasmit.superadmin.common.ApiResponse;
import com.plasmit.superadmin.impersonation.dto.response.ImpersonationResponse;
import com.plasmit.superadmin.impersonation.service.ImpersonationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/super-admin")
public class ImpersonationController {

    private final ImpersonationService impersonationService;

    public ImpersonationController(ImpersonationService impersonationService) {
        this.impersonationService = impersonationService;
    }

    @PostMapping("/hospitals/{hospitalId}/impersonate")
    public ApiResponse<ImpersonationResponse> impersonate(
            @PathVariable Long hospitalId) {

        return ApiResponse.success(
                "Impersonation started",
                impersonationService.impersonate(hospitalId)
        );
    }

    @PostMapping("/impersonation/exit")
    public ApiResponse<?> exit(@RequestHeader("Authorization") String token) {

        impersonationService.exitImpersonation(token);

        return ApiResponse.success("Exited impersonation", null);
    }
}