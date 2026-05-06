package com.plasmit.superadmin.repository;

import com.plasmit.superadmin.dto.response.FeatureFlagResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SettingsRepository {

    private static final Logger log = LoggerFactory.getLogger(SettingsRepository.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SettingsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String findProjectSettingsJson() {
        String sql = """
                SELECT setting_value
                FROM global_settings
                WHERE setting_key = 'project_settings'
                LIMIT 1
                """;

        log.debug("Fetching project settings JSON");

        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), String.class);
    }

    public int updateProjectSettings(String jsonValue, Long updatedBy) {
        String sql = """
                UPDATE global_settings
                SET setting_value = CAST(:jsonValue AS JSON),
                    updated_by = :updatedBy,
                    updated_at = NOW()
                WHERE setting_key = 'project_settings'
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("jsonValue", jsonValue)
                .addValue("updatedBy", updatedBy);

        int updated = jdbcTemplate.update(sql, params);
        log.debug("Project settings updated. affectedRows={}", updated);
        return updated;
    }

    public List<FeatureFlagResponse> findFeatureFlags() {
        String sql = """
                SELECT flag_key, flag_name, enabled
                FROM feature_flags
                ORDER BY id ASC
                """;

        log.debug("Fetching feature flags");

        return jdbcTemplate.query(sql, new MapSqlParameterSource(), (rs, rowNum) ->
                new FeatureFlagResponse(
                        rs.getString("flag_key"),
                        rs.getString("flag_name"),
                        rs.getBoolean("enabled")
                )
        );
    }

    public int updateFeatureFlag(String key, boolean enabled, Long updatedBy) {
        String sql = """
                UPDATE feature_flags
                SET enabled = :enabled,
                    updated_by = :updatedBy,
                    updated_at = NOW()
                WHERE flag_key = :key
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("key", key)
                .addValue("enabled", enabled ? 1 : 0)
                .addValue("updatedBy", updatedBy);

        int updated = jdbcTemplate.update(sql, params);

        log.debug("Feature flag updated. key={}, enabled={}, affectedRows={}",
                key, enabled, updated);

        return updated;
    }
}