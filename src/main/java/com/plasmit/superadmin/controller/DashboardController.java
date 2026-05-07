package com.plasmit.superadmin.controller;

import com.plasmit.superadmin.common.ApiResponse;
import com.plasmit.superadmin.dto.response.DashboardKpiResponse;
import com.plasmit.superadmin.dto.response.DashboardSummaryResponse;
import com.plasmit.superadmin.security.TenantContext;
import com.plasmit.superadmin.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/super-admin/dashboard")
@CrossOrigin("*")
public class DashboardController {

    private static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/kpis")
    public ApiResponse<DashboardKpiResponse> getKpis() {
        log.info("Dashboard KPI API called. userId={}, role={}",
                TenantContext.getUserId(), TenantContext.getRole());

        return ApiResponse.success("Dashboard KPIs fetched", dashboardService.getKpis());
    }

    @GetMapping("/summary")
    public ApiResponse<DashboardSummaryResponse> getSummary() {
        log.info("Dashboard summary API called. userId={}, role={}",
                TenantContext.getUserId(), TenantContext.getRole());

        return ApiResponse.success("Dashboard summary fetched", dashboardService.getSummary());
    }

    @GetMapping("/hospital-count")
    public ApiResponse<Integer> getHospitalCount() {
        log.info("Dashboard hospital count API called. userId={}, role={}",
                TenantContext.getUserId(), TenantContext.getRole());

        return ApiResponse.success("Hospital count fetched", dashboardService.getHospitalCount());
    }
}
