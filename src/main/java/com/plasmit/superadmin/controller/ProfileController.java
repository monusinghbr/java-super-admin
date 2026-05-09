package com.plasmit.superadmin.controller;

import com.plasmit.superadmin.common.ApiResponse;
import com.plasmit.superadmin.dto.request.ChangePasswordRequest;
import com.plasmit.superadmin.dto.request.UpdateMfaRequest;
import com.plasmit.superadmin.dto.request.UpdateProfileRequest;
import com.plasmit.superadmin.dto.response.ProfileResponse;
import com.plasmit.superadmin.dto.response.SessionResponse;
import com.plasmit.superadmin.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/super-admin/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ApiResponse<ProfileResponse> getProfile() {
        return ApiResponse.success("Profile fetched", profileService.getProfile());
    }

    @PutMapping
    public ApiResponse<ProfileResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        return ApiResponse.success("Profile updated successfully", profileService.updateProfile(request));
    }

    @PutMapping("/password")
    public ApiResponse<Object> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        profileService.changePassword(request);
        return ApiResponse.success("Password changed successfully", null);
    }

    @PutMapping("/mfa")
    public ApiResponse<Object> updateMfa(@RequestBody UpdateMfaRequest request) {
        profileService.updateMfa(request);
        return ApiResponse.success("MFA updated successfully", null);
    }

    @GetMapping("/sessions")
    public ApiResponse<List<SessionResponse>> getSessions() {
        return ApiResponse.success("Sessions fetched", profileService.getSessions());
    }

    @DeleteMapping("/sessions/{sessionId}")
    public ApiResponse<Object> revokeSession(@PathVariable Long sessionId) {
        profileService.revokeSession(sessionId);
        return ApiResponse.success("Session revoked successfully", null);
    }

    @DeleteMapping("/sessions")
    public ApiResponse<Object> revokeOtherSessions() {
        profileService.revokeOtherSessions();
        return ApiResponse.success("All other sessions revoked successfully", null);
    }
}
