package com.plasmit.superadmin.service.impl;

import com.plasmit.superadmin.dto.request.CreateHospitalRequest;
import com.plasmit.superadmin.dto.request.UpdateHospitalRequest;
import com.plasmit.superadmin.dto.request.UpdateHospitalStatusRequest;
import com.plasmit.superadmin.dto.response.HospitalResponse;
import com.plasmit.superadmin.repository.HospitalRepository;
import com.plasmit.superadmin.security.TenantContext;
import com.plasmit.superadmin.service.HospitalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {

    private static final Logger log = LoggerFactory.getLogger(HospitalServiceImpl.class);

    private final HospitalRepository hospitalRepository;
    

    public HospitalServiceImpl(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    @Transactional
    public Map<String, Object> createHospital(CreateHospitalRequest request) {
        Long userId = TenantContext.getUserId();

        log.info("Creating hospital. code={}, name={}, createdBy={}",
                request.getCode(), request.getName(), userId);

        validateStatus(request.getStatus());

        Long tenantId = hospitalRepository.createTenant(request.getCode(), request.getName());
        Long hospitalId = hospitalRepository.createHospital(request, tenantId, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("id", hospitalId);
        response.put("tenantId", tenantId);
        response.put("code", request.getCode());

        log.info("Hospital created successfully. hospitalId={}, tenantId={}", hospitalId, tenantId);
        return response;
    }

    @Override
    public List<HospitalResponse> getHospitals(String search, String status) {
        log.info("Fetching hospitals. search={}, status={}, userId={}",
                search, status, TenantContext.getUserId());

        return hospitalRepository.findAll(search, status);
    }

    @Override
    public HospitalResponse getHospitalById(Long id) {
        log.info("Fetching hospital detail. id={}, userId={}", id, TenantContext.getUserId());

        return hospitalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hospital not found"));
    }

    @Override
    public HospitalResponse updateHospital(Long id, UpdateHospitalRequest request) {
        Long userId = TenantContext.getUserId();

        log.info("Updating hospital. id={}, updatedBy={}", id, userId);

        int updated = hospitalRepository.updateHospital(id, request, userId);
        if (updated == 0) {
            throw new IllegalArgumentException("Hospital not found or update failed");
        }

        return getHospitalById(id);
    }

    @Override
    public Map<String, Object> updateHospitalStatus(Long id, UpdateHospitalStatusRequest request) {
        Long userId = TenantContext.getUserId();

        validateStatus(request.getStatus());

        log.info("Updating hospital status. id={}, status={}, reason={}, updatedBy={}",
                id, request.getStatus(), request.getReason(), userId);

        int updated = hospitalRepository.updateStatus(id, request.getStatus(), userId);
        if (updated == 0) {
            throw new IllegalArgumentException("Hospital not found or status update failed");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("status", request.getStatus());

        return response;
    }

    private void validateStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalArgumentException("Status is required");
        }

        if (!status.equalsIgnoreCase("ACTIVE")
                && !status.equalsIgnoreCase("INACTIVE")
                && !status.equalsIgnoreCase("SUSPENDED")) {
            throw new IllegalArgumentException("Invalid hospital status");
        }
    }
}