package com.plasmit.superadmin.service;

import com.plasmit.superadmin.dto.request.ChangePasswordRequest;
import com.plasmit.superadmin.dto.request.UpdateMfaRequest;
import com.plasmit.superadmin.dto.request.UpdateProfileRequest;
import com.plasmit.superadmin.dto.response.ProfileResponse;
import com.plasmit.superadmin.dto.response.SessionResponse;

import java.util.List;

public interface ProfileService {

    ProfileResponse getProfile();

    ProfileResponse updateProfile(UpdateProfileRequest request);

    void changePassword(ChangePasswordRequest request);

    void updateMfa(UpdateMfaRequest request);

    List<SessionResponse> getSessions();

    void revokeSession(Long sessionId);

    void revokeOtherSessions();
}