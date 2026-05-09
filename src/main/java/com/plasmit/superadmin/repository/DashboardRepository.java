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
        String sql = """
                SELECT COUNT(*)
                FROM hospital_subscriptions
                WHERE status = 'ACTIVE'
                  AND is_deleted = 0
                """;
        return getCount(sql);
    }

    public int countExpiringPlansNext30Days() {
        String sql = """
                SELECT COUNT(*)
                FROM hospital_subscriptions
                WHERE status = 'ACTIVE'
                  AND is_deleted = 0
                  AND COALESCE(renewal_date, end_date)
                      BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY)
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
                FROM billing_payments
                WHERE is_deleted = 0
                  AND UPPER(payment_status) NOT IN ('SUCCESS', 'PAID')
                """;
        return getCount(sql);
    }

    public int calculateMfaEnabledPercentage() {
        String sql = """
                SELECT COALESCE(ROUND(
                    100 * SUM(CASE WHEN mfa_enabled = 1 THEN 1 ELSE 0 END) / NULLIF(COUNT(*), 0)
                ), 0)
                FROM users
                WHERE status = 'ACTIVE'
                  AND is_deleted = 0
                """;
        return getNumber(sql);
    }

    public int calculateRbacReviewedPercentage() {
        String sql = """
                SELECT COALESCE(ROUND(
                    100 * SUM(CASE WHEN permission_count > 0 THEN 1 ELSE 0 END) / NULLIF(COUNT(*), 0)
                ), 0)
                FROM (
                    SELECT r.id, COUNT(rp.permission_id) AS permission_count
                    FROM roles r
                    LEFT JOIN role_permissions rp ON rp.role_id = r.id
                    WHERE r.is_deleted = 0
                    GROUP BY r.id
                ) role_summary
                """;
        return getNumber(sql);
    }

    public int calculateAuditRetentionHealthyPercentage() {
        String sql = """
                SELECT COALESCE(ROUND(
                    100 * SUM(CASE WHEN created_at >= DATE_SUB(NOW(), INTERVAL 90 DAY) THEN 1 ELSE 0 END)
                    / NULLIF(COUNT(*), 0)
                ), 0)
                FROM audit_logs
                """;
        return getNumber(sql);
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
                    CAST(h.id AS CHAR) AS hospital_id,
                    h.hospital_name,
                    h.hospital_code,
                    CASE
                        WHEN hs.id IS NULL THEN 'Subscription not assigned'
                        WHEN hs.plan_id IS NULL THEN 'Subscription assignment'
                        WHEN hs.payment_status <> 'PAID' THEN 'Payment pending'
                        WHEN h.status <> 'ACTIVE' THEN 'Activation pending'
                        ELSE 'Onboarded'
                    END AS current_step,
                    COALESCE(h.contact_person, 'Unassigned') AS owner
                FROM hospitals h
                LEFT JOIN hospital_subscriptions hs
                       ON hs.hospital_id = h.id
                      AND hs.is_deleted = 0
                WHERE h.is_deleted = 0
                  AND (
                      h.status IN ('PENDING', 'INACTIVE')
                      OR hs.id IS NULL
                      OR hs.plan_id IS NULL
                      OR hs.payment_status <> 'PAID'
                  )
                ORDER BY h.created_at DESC
                LIMIT 10
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new DashboardSummaryResponse.OnboardingQueueItem(
                        rs.getString("hospital_id"),
                        rs.getString("hospital_name"),
                        rs.getString("hospital_code"),
                        rs.getString("current_step"),
                        rs.getString("owner")
                )
        );
    }

    private int getCount(String sql) {
        Integer value = jdbcTemplate.queryForObject(sql, new org.springframework.jdbc.core.namedparam.MapSqlParameterSource(), Integer.class);
        return value == null ? 0 : value;
    }

    private int getNumber(String sql) {
        Number value = jdbcTemplate.queryForObject(sql, new org.springframework.jdbc.core.namedparam.MapSqlParameterSource(), Number.class);
        return value == null ? 0 : value.intValue();
    }
}
