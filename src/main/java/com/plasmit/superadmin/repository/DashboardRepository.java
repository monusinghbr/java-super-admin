package com.plasmit.superadmin.repository;

import com.plasmit.superadmin.dto.response.DashboardSummaryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DashboardRepository {

    private static final Logger log = LoggerFactory.getLogger(DashboardRepository.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public DashboardRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int countTotalHospitals() {
        String sql = "SELECT COUNT(*) FROM tenants WHERE tenant_type = 'HOSPITAL' AND is_deleted = 0";
        log.debug("Counting total hospitals");
        return getCount(sql);
    }

    public int countHospitalsByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM tenants WHERE tenant_type = 'HOSPITAL' AND status = :status AND is_deleted = 0";
        return jdbcTemplate.queryForObject(sql, new org.springframework.jdbc.core.namedparam.MapSqlParameterSource("status", status), Integer.class);
    }

    public int countActiveSubscriptions() {
        String sql = "SELECT COUNT(*) FROM hospital_subscriptions WHERE status = 'ACTIVE' AND is_deleted = 0";
        return getCount(sql);
    }

    public int countExpiringPlansNext30Days() {
        String sql = """
                SELECT COUNT(*)
                FROM hospital_subscriptions
                WHERE status = 'ACTIVE'
                  AND is_deleted = 0
                  AND end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY)
                """;
        return getCount(sql);
    }

    public int countPlatformUsers() {
        String sql = "SELECT COUNT(*) FROM users WHERE is_deleted = 0";
        return getCount(sql);
    }

    public int countFailedPayments() {
        String sql = """
                SELECT COUNT(*)
                FROM hospital_subscriptions
                WHERE payment_status IN ('FAILED', 'PENDING', 'OVERDUE')
                  AND is_deleted = 0
                """;
        return getCount(sql);
    }

    public int calculateMfaEnabledPercentage() {
        String sql = """
                SELECT 
                    CASE 
                        WHEN COUNT(*) = 0 THEN 0
                        ELSE ROUND((SUM(CASE WHEN mfa_enabled = 1 THEN 1 ELSE 0 END) / COUNT(*)) * 100)
                    END
                FROM users
                WHERE is_deleted = 0
                """;
        Integer value = jdbcTemplate.queryForObject(sql, new org.springframework.jdbc.core.namedparam.MapSqlParameterSource(), Integer.class);
        return value == null ? 0 : value;
    }

    public List<DashboardSummaryResponse.CriticalAlert> findCriticalAlerts() {
        String sql = """
                SELECT 
                    CAST(id AS CHAR) AS id,
                    event AS title,
                    detail AS message,
                    category,
                    severity
                FROM audit_logs
                WHERE severity IN ('HIGH', 'CRITICAL')
                ORDER BY created_at DESC
                LIMIT 5
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new DashboardSummaryResponse.CriticalAlert(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("message"),
                        rs.getString("category"),
                        rs.getString("severity")
                )
        );
    }

    public List<DashboardSummaryResponse.OnboardingQueueItem> findOnboardingQueue() {
        String sql = """
                SELECT 
                    CAST(id AS CHAR) AS hospital_id,
                    tenant_name,
                    tenant_code,
                    status
                FROM tenants
                WHERE tenant_type = 'HOSPITAL'
                  AND is_deleted = 0
                  AND status IN ('PENDING', 'INACTIVE')
                ORDER BY created_at DESC
                LIMIT 10
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new DashboardSummaryResponse.OnboardingQueueItem(
                        rs.getString("hospital_id"),
                        rs.getString("tenant_name"),
                        rs.getString("tenant_code"),
                        "Subscription assignment",
                        "Operations Admin"
                )
        );
    }

    private int getCount(String sql) {
        Integer value = jdbcTemplate.queryForObject(sql, new org.springframework.jdbc.core.namedparam.MapSqlParameterSource(), Integer.class);
        return value == null ? 0 : value;
    }
}