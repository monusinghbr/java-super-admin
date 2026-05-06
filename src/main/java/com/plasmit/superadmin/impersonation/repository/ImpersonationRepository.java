package com.plasmit.superadmin.impersonation.repository;

import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Repository;

@Repository
public class ImpersonationRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ImpersonationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createSession(Long adminId,
                              Long hospitalId,
                              Long tenantId,
                              String token,
                              String expiresAt) {

        String sql = """
            INSERT INTO impersonation_sessions
            (super_admin_id, hospital_id, tenant_id, token, expires_at)
            VALUES (:adminId, :hospitalId, :tenantId, :token, :expiresAt)
        """;

        jdbcTemplate.update(sql,
                new MapSqlParameterSource()
                        .addValue("adminId", adminId)
                        .addValue("hospitalId", hospitalId)
                        .addValue("tenantId", tenantId)
                        .addValue("token", token)
                        .addValue("expiresAt", expiresAt)
        );
    }

    public void deactivateSession(String token) {

        String sql = """
            UPDATE impersonation_sessions
            SET active = 0
            WHERE token = :token
        """;

        jdbcTemplate.update(sql,
                new MapSqlParameterSource("token", token));
    }
}