package com.plasmit.superadmin.service;

import com.plasmit.superadmin.dto.response.DashboardKpiResponse;
import com.plasmit.superadmin.dto.response.DashboardSummaryResponse;

public interface DashboardService {

    DashboardKpiResponse getKpis();

    DashboardSummaryResponse getSummary();
}