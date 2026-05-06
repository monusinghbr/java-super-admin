package com.plasmit.superadmin.audit.repository;

import com.plasmit.superadmin.audit.dto.response.AuditLogResponse;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuditLogRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AuditLogRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Long userId,
                     String role,
                     String action,
                     String entityType,
                     Long entityId,
                     String description,
                     String ipAddress) {

        String sql = """
                INSERT INTO audit_logs
                (
                    actor_id,
                    actor_name,
                    event,
                    target_type,
                    target_id,
                    category,
                    severity,
                    detail,
                    ip_address
                )
                VALUES
                (
                    :actorId,
                    :actorName,
                    :event,
                    :targetType,
                    :targetId,
                    :category,
                    :severity,
                    :detail,
                    :ipAddress
                )
                """;

        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("actorId", userId)
                .addValue("actorName", role)
                .addValue("event", action)
                .addValue("targetType", entityType)
                .addValue("targetId", entityId == null ? null : String.valueOf(entityId))
                .addValue("category", entityType)
                .addValue("severity", "LOW")
                .addValue("detail", description)
                .addValue("ipAddress", ipAddress));
    }

    public List<AuditLogResponse> findLogs(String action,
                                           String entityType,
                                           Long userId) {

        StringBuilder sql = new StringBuilder("""
                SELECT
                    id,
                    actor_id AS user_id,
                    actor_name AS user_role,
                    event AS action,
                    target_type AS entity_type,
                    target_id AS entity_id,
                    detail AS description,
                    ip_address,
                    created_at
                FROM audit_logs
                WHERE 1 = 1
                """);

        MapSqlParameterSource params = new MapSqlParameterSource();

        if (action != null && !action.isBlank()) {
            sql.append(" AND event = :action ");
            params.addValue("action", action);
        }

        if (entityType != null && !entityType.isBlank()) {
            sql.append(" AND target_type = :entityType ");
            params.addValue("entityType", entityType);
        }

        if (userId != null) {
            sql.append(" AND actor_id = :userId ");
            params.addValue("userId", userId);
        }

        sql.append(" ORDER BY created_at DESC ");

        return jdbcTemplate.query(sql.toString(), params,
                (rs, rowNum) -> new AuditLogResponse(
                        rs.getLong("id"),
                        rs.getObject("user_id") == null ? null : rs.getLong("user_id"),
                        rs.getString("user_role"),
                        rs.getString("action"),
                        rs.getString("entity_type"),
                        rs.getObject("entity_id") == null ? null : Long.valueOf(rs.getString("entity_id")),
                        rs.getString("description"),
                        rs.getString("ip_address"),
                        rs.getString("created_at")
                ));
    }
}