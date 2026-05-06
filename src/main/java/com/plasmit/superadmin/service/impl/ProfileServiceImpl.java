package com.plasmit.superadmin.service.impl;

import com.plasmit.superadmin.dto.request.ChangePasswordRequest;
import com.plasmit.superadmin.dto.request.UpdateMfaRequest;
import com.plasmit.superadmin.dto.request.UpdateProfileRequest;
import com.plasmit.superadmin.dto.response.ProfileResponse;
import com.plasmit.superadmin.dto.response.SessionResponse;
import com.plasmit.superadmin.repository.ProfileRepository;
import com.plasmit.superadmin.security.TenantContext;
import com.plasmit.superadmin.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {

    private static final Logger log = LoggerFactory.getLogger(ProfileServiceImpl.class);

    private final ProfileRepository profileRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public ProfileResponse getProfile() {
        Long userId = TenantContext.getUserId();
        log.info("Fetching profile. userId={}", userId);

        return profileRepository.findProfileById(userId)
                .orElseThrow(() -> new BadCredentialsException("Profile not found"));
    }

    @Override
    public ProfileResponse updateProfile(UpdateProfileRequest request) {
        Long userId = TenantContext.getUserId();
        log.info("Updating profile. userId={}", userId);

        int updated = profileRepository.updateProfile(userId, request);
        if (updated == 0) {
            throw new BadCredentialsException("Profile update failed");
        }

        return getProfile();
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        Long userId = TenantContext.getUserId();
        log.info("Changing password. userId={}", userId);

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Confirm password does not match");
        }

        String currentHash = profileRepository.findPasswordHash(userId);

        if (!passwordEncoder.matches(request.getCurrentPassword().trim(), currentHash)) {
            log.warn("Password change failed. Invalid current password. userId={}", userId);
            throw new BadCredentialsException("Current password is incorrect");
        }

        String newHash = passwordEncoder.encode(request.getNewPassword().trim());
        profileRepository.updatePassword(userId, newHash);
    }

    @Override
    public void updateMfa(UpdateMfaRequest request) {
        Long userId = TenantContext.getUserId();
        log.info("Updating MFA. userId={}, enabled={}", userId, request.isEnabled());

        profileRepository.updateMfa(userId, request.isEnabled());
    }

    @Override
    public List<SessionResponse> getSessions() {
        Long userId = TenantContext.getUserId();
        log.info("Fetching sessions. userId={}", userId);

        return profileRepository.findSessions(userId);
    }

    @Override
    public void revokeSession(Long sessionId) {
        Long userId = TenantContext.getUserId();
        log.info("Revoking session. userId={}, sessionId={}", userId, sessionId);

        profileRepository.revokeSession(userId, sessionId);
    }

    @Override
    public void revokeOtherSessions() {
        Long userId = TenantContext.getUserId();
        log.info("Revoking other sessions. userId={}", userId);

        profileRepository.revokeOtherSessions(userId);
    }
}