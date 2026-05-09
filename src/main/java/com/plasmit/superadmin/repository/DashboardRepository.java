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
        String sql = "SELECT COUNT(*) FROM hospitals WHERE is_deleted = 0";
        log.debug("Counting total hospitals");
        return getCount(sql);
    }

    public int countHospitalsByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM hospitals WHERE status = :status AND is_deleted = 0";
        return jdbcTemplate.queryForObject(sql, new org.springframework.jdbc.core.namedparam.MapSqlParameterSource("status", status), Integer.class);
    }

    public int countActiveSubscriptions() {
        return 0;
    }

    public int countExpiringPlansNext30Days() {
        return 0;
    }

    public int countPlatformUsers() {
        String sql = "SELECT COUNT(*) FROM users WHERE is_deleted = 0";
        return getCount(sql);
    }

    public int countFailedPayments() {
        return 0;
    }

    public int calculateMfaEnabledPercentage() {
        return 0;
    }

    public List<DashboardSummaryResponse.CriticalAlert> findCriticalAlerts() {
        String sql = """
                SELECT
                    CAST(id AS CHAR) AS id,
                    event AS title,
                    COALESCE(detail, '') AS message,
                    COALESCE(category, target_type, 'AUDIT') AS category,
                    COALESCE(severity, 'LOW') AS severity
                FROM audit_logs
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
                    hospital_name,
                    hospital_code,
                    status
                FROM hospitals
                WHERE is_deleted = 0
                  AND status IN ('PENDING', 'INACTIVE')
                ORDER BY created_at DESC
                LIMIT 10
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new DashboardSummaryResponse.OnboardingQueueItem(
                        rs.getString("hospital_id"),
                        rs.getString("hospital_name"),
                        rs.getString("hospital_code"),
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
