package com.plasmit.superadmin.service;

import com.plasmit.superadmin.dto.request.CreateHospitalRequest;
import com.plasmit.superadmin.dto.request.UpdateHospitalRequest;
import com.plasmit.superadmin.dto.request.UpdateHospitalStatusRequest;
import com.plasmit.superadmin.dto.response.HospitalResponse;

import java.util.List;
import java.util.Map;

public interface HospitalService {

    Map<String, Object> createHospital(CreateHospitalRequest request);

    List<HospitalResponse> getHospitals(String search, String status);

    HospitalResponse getHospitalById(Long id);

    HospitalResponse updateHospital(Long id, UpdateHospitalRequest request);

    Map<String, Object> updateHospitalStatus(Long id, UpdateHospitalStatusRequest request);
}