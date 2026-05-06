package com.plasmit.superadmin.impersonation.service;

import com.plasmit.superadmin.impersonation.dto.response.ImpersonationResponse;

public interface ImpersonationService {

    ImpersonationResponse impersonate(Long hospitalId);

    void exitImpersonation(String token);
}