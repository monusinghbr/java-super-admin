package com.plasmit.superadmin.impersonation.service.impl;

import com.plasmit.superadmin.impersonation.dto.response.ImpersonationResponse;
import com.plasmit.superadmin.impersonation.repository.ImpersonationRepository;
import com.plasmit.superadmin.repository.HospitalRepository;
import com.plasmit.superadmin.security.JwtService;
import com.plasmit.superadmin.security.TenantContext;
import com.plasmit.superadmin.impersonation.service.ImpersonationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ImpersonationServiceImpl implements ImpersonationService {

    private final HospitalRepository hospitalRepository;
    private final ImpersonationRepository impersonationRepository;
    private final JwtService jwtService;

    public ImpersonationServiceImpl(HospitalRepository hospitalRepository,
                                    ImpersonationRepository impersonationRepository,
                                    JwtService jwtService) {

        this.hospitalRepository = hospitalRepository;
        this.impersonationRepository = impersonationRepository;
        this.jwtService = jwtService;
    }

    @Override
    public ImpersonationResponse impersonate(Long hospitalId) {

        Long adminId = TenantContext.getUserId();

        var hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found"));

        Long tenantId = hospital.getTenantId();

        String token = jwtService.generateImpersonationToken(
                adminId,
                hospitalId,
                tenantId
        );

        LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);

        impersonationRepository.createSession(
                adminId,
                hospitalId,
                tenantId,
                token,
                expiry.toString()
        );

        return new ImpersonationResponse(
                token,
                hospitalId,
                tenantId,
                expiry.toString()
        );
    }

    @Override
    public void exitImpersonation(String token) {

        impersonationRepository.deactivateSession(token);
    }
}