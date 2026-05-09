package com.plasmit.superadmin.controller;

import com.plasmit.superadmin.common.ApiResponse;
import com.plasmit.superadmin.dto.request.CreateHospitalRequest;
import com.plasmit.superadmin.dto.request.UpdateHospitalRequest;
import com.plasmit.superadmin.dto.request.UpdateHospitalStatusRequest;
import com.plasmit.superadmin.dto.response.HospitalResponse;
import com.plasmit.superadmin.service.HospitalService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/super-admin/hospitals")
public class HospitalController {

    private static final Logger log = LoggerFactory.getLogger(HospitalController.class);

    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PostMapping
    public ApiResponse<Map<String, Object>> createHospital(@Valid @RequestBody CreateHospitalRequest request) {
        log.info("Create hospital API called. code={}", request.getCode());
        return ApiResponse.success("Hospital created successfully", hospitalService.createHospital(request));
    }

    @GetMapping
    public ApiResponse<List<HospitalResponse>> getHospitals(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status
    ) {
        log.info("List hospitals API called. search={}, status={}", search, status);
        return ApiResponse.success("Hospitals fetched", hospitalService.getHospitals(search, status));
    }

    @GetMapping("/{hospitalId}")
    public ApiResponse<HospitalResponse> getHospital(@PathVariable Long hospitalId) {
        log.info("Hospital detail API called. hospitalId={}", hospitalId);
        return ApiResponse.success("Hospital fetched", hospitalService.getHospitalById(hospitalId));
    }

    @PutMapping("/{hospitalId}")
    public ApiResponse<HospitalResponse> updateHospital(
            @PathVariable Long hospitalId,
            @Valid @RequestBody UpdateHospitalRequest request
    ) {
        log.info("Update hospital API called. hospitalId={}", hospitalId);
        return ApiResponse.success("Hospital updated successfully", hospitalService.updateHospital(hospitalId, request));
    }

    @PatchMapping("/{hospitalId}/status")
    public ApiResponse<Map<String, Object>> updateStatus(
            @PathVariable Long hospitalId,
            @Valid @RequestBody UpdateHospitalStatusRequest request
    ) {
        log.info("Update hospital status API called. hospitalId={}, status={}", hospitalId, request.getStatus());
        return ApiResponse.success("Hospital status updated successfully",
                hospitalService.updateHospitalStatus(hospitalId, request));
    }
}
