package com.plasmit.superadmin.service.impl;

import com.plasmit.superadmin.dto.response.DashboardKpiResponse;
import com.plasmit.superadmin.dto.response.DashboardSummaryResponse;
import com.plasmit.superadmin.repository.DashboardRepository;
import com.plasmit.superadmin.security.TenantContext;
import com.plasmit.superadmin.service.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);

    private final DashboardRepository dashboardRepository;

    public DashboardServiceImpl(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    @Override
    public DashboardKpiResponse getKpis() {
        log.info("Fetching dashboard KPIs. userId={}, role={}",
                TenantContext.getUserId(), TenantContext.getRole());

        return new DashboardKpiResponse(
                dashboardRepository.countTotalHospitals(),
                dashboardRepository.countHospitalsByStatus("ACTIVE"),
                dashboardRepository.countHospitalsByStatus("INACTIVE"),
                dashboardRepository.countHospitalsByStatus("SUSPENDED"),
                dashboardRepository.countActiveSubscriptions(),
                dashboardRepository.countExpiringPlansNext30Days(),
                dashboardRepository.countPlatformUsers(),
                dashboardRepository.countFailedPayments()
        );
    }

    @Override
    public DashboardSummaryResponse getSummary() {
        log.info("Fetching dashboard summary. userId={}, role={}",
                TenantContext.getUserId(), TenantContext.getRole());

        DashboardSummaryResponse.SecurityPosture securityPosture =
                new DashboardSummaryResponse.SecurityPosture(
                        dashboardRepository.calculateMfaEnabledPercentage(),
                        dashboardRepository.calculateRbacReviewedPercentage(),
                        dashboardRepository.calculateAuditRetentionHealthyPercentage()
                );

        return new DashboardSummaryResponse(
                dashboardRepository.findCriticalAlerts(),
                securityPosture,
                dashboardRepository.findOnboardingQueue()
        );
    }

    @Override
    public int getHospitalCount() {
        log.info("Fetching hospital count. userId={}, role={}",
                TenantContext.getUserId(), TenantContext.getRole());

        return dashboardRepository.countTotalHospitals();
    }
}
