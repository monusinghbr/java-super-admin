package com.plasmit.superadmin.repository;

import com.plasmit.superadmin.dto.request.UpdateProfileRequest;
import com.plasmit.superadmin.dto.response.ProfileResponse;
import com.plasmit.superadmin.dto.response.SessionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProfileRepository {

    private static final Logger log = LoggerFactory.getLogger(ProfileRepository.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ProfileRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<ProfileResponse> findProfileById(Long userId) {
        String sql = """
                SELECT id, name, email, role_code, avatar_url, mfa_enabled, last_login_at
                FROM users
                WHERE id = :userId
                  AND is_deleted = 0
                LIMIT 1
                """;

        try {
            ProfileResponse response = jdbcTemplate.queryForObject(
                    sql,
                    new MapSqlParameterSource("userId", userId),
                    (rs, rowNum) -> new ProfileResponse(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("role_code"),
                            rs.getString("avatar_url"),
                            rs.getBoolean("mfa_enabled"),
                            rs.getString("last_login_at")
                    )
            );
            return Optional.ofNullable(response);
        } catch (EmptyResultDataAccessException ex) {
            log.warn("Profile not found. userId={}", userId);
            return Optional.empty();
        }
    }

    public String findPasswordHash(Long userId) {
        String sql = "SELECT password_hash FROM users WHERE id = :userId AND is_deleted = 0";
        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("userId", userId), String.class);
    }

    public int updateProfile(Long userId, UpdateProfileRequest request) {
        String sql = """
                UPDATE users
                SET name = :name,
                    email = :email,
                    avatar_url = :avatarUrl,
                    updated_at = NOW()
                WHERE id = :userId
                  AND is_deleted = 0
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("name", request.getName())
                .addValue("email", request.getEmail())
                .addValue("avatarUrl", request.getAvatarUrl());

        return jdbcTemplate.update(sql, params);
    }

    public int updatePassword(Long userId, String newPasswordHash) {
        String sql = """
                UPDATE users
                SET password_hash = :passwordHash,
                    updated_at = NOW()
                WHERE id = :userId
                  AND is_deleted = 0
                """;

        return jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("passwordHash", newPasswordHash));
    }

    public int updateMfa(Long userId, boolean enabled) {
        String sql = """
                UPDATE users
                SET mfa_enabled = :enabled,
                    updated_at = NOW()
                WHERE id = :userId
                  AND is_deleted = 0
                """;

        return jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("enabled", enabled ? 1 : 0));
    }

    public List<SessionResponse> findSessions(Long userId) {
        String sql = """
                SELECT id, device, browser, ip_address, location, last_active_at, is_current
                FROM user_sessions
                WHERE user_id = :userId
                  AND revoked = 0
                ORDER BY last_active_at DESC
                """;

        return jdbcTemplate.query(sql, new MapSqlParameterSource("userId", userId),
                (rs, rowNum) -> new SessionResponse(
                        rs.getLong("id"),
                        rs.getString("device"),
                        rs.getString("browser"),
                        rs.getString("ip_address"),
                        rs.getString("location"),
                        rs.getString("last_active_at"),
                        rs.getBoolean("is_current")
                ));
    }

    public int revokeSession(Long userId, Long sessionId) {
        String sql = """
                UPDATE user_sessions
                SET revoked = 1
                WHERE id = :sessionId
                  AND user_id = :userId
                """;

        return jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("sessionId", sessionId));
    }

    public int revokeOtherSessions(Long userId) {
        String sql = """
                UPDATE user_sessions
                SET revoked = 1
                WHERE user_id = :userId
                  AND is_current = 0
                """;

        return jdbcTemplate.update(sql, new MapSqlParameterSource("userId", userId));
    }
}