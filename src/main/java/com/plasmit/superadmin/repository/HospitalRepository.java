/*
 * package com.plasmit.superadmin.repository;
 * 
 * import com.plasmit.superadmin.dto.request.CreateHospitalRequest; import
 * com.plasmit.superadmin.dto.request.UpdateHospitalRequest; import
 * com.plasmit.superadmin.dto.response.HospitalResponse; import
 * org.slf4j.Logger; import org.slf4j.LoggerFactory; import
 * org.springframework.dao.EmptyResultDataAccessException; import
 * org.springframework.jdbc.core.namedparam.*; import
 * org.springframework.stereotype.Repository; import
 * org.springframework.jdbc.support.GeneratedKeyHolder; import
 * org.springframework.jdbc.support.KeyHolder;
 * 
 * import java.util.List; import java.util.Optional;
 * 
 * @Repository public class HospitalRepository {
 * 
 * private static final Logger log =
 * LoggerFactory.getLogger(HospitalRepository.class);
 * 
 * private final NamedParameterJdbcTemplate jdbcTemplate;
 * 
 * public HospitalRepository(NamedParameterJdbcTemplate jdbcTemplate) {
 * this.jdbcTemplate = jdbcTemplate; }
 * 
 * public Long createTenant(String code, String name) { String sql = """ INSERT
 * INTO tenants (tenant_code, tenant_name, tenant_type, status) VALUES
 * (:tenantCode, :tenantName, 'HOSPITAL', 'ACTIVE') """;
 * 
 * KeyHolder keyHolder = new GeneratedKeyHolder();
 * 
 * jdbcTemplate.update(sql, new MapSqlParameterSource() .addValue("tenantCode",
 * code) .addValue("tenantName", name), keyHolder);
 * 
 * Number key = keyHolder.getKey(); Long tenantId = key == null ? null :
 * key.longValue();
 * 
 * log.debug("Tenant created. tenantId={}, code={}", tenantId, code); return
 * tenantId; }
 * 
 * public Long createHospital(CreateHospitalRequest request, Long tenantId, Long
 * createdBy) { String sql = """ INSERT INTO hospitals (tenant_id, name, code,
 * contact_email, contact_phone, country, state, city, pincode, timezone,
 * subscription_plan_id, status, created_by) VALUES (:tenantId, :name, :code,
 * :contactEmail, :contactPhone, :country, :state, :city, :pincode, :timezone,
 * :subscriptionPlanId, :status, :createdBy) """;
 * 
 * KeyHolder keyHolder = new GeneratedKeyHolder();
 * 
 * jdbcTemplate.update(sql, new MapSqlParameterSource() .addValue("tenantId",
 * tenantId) .addValue("name", request.getName()) .addValue("code",
 * request.getCode()) .addValue("contactEmail", request.getContactEmail())
 * .addValue("contactPhone", request.getContactPhone()) .addValue("country",
 * request.getCountry()) .addValue("state", request.getState())
 * .addValue("city", request.getCity()) .addValue("pincode",
 * request.getPincode()) .addValue("timezone", request.getTimezone())
 * .addValue("subscriptionPlanId", request.getSubscriptionPlanId())
 * .addValue("status", request.getStatus()) .addValue("createdBy", createdBy),
 * keyHolder);
 * 
 * Number key = keyHolder.getKey(); return key == null ? null : key.longValue();
 * }
 * 
 * public List<HospitalResponse> findAll(String search, String status) {
 * StringBuilder sql = new StringBuilder(""" SELECT id, tenant_id, name, code,
 * contact_email, contact_phone, country, state, city, pincode, timezone,
 * subscription_plan_id, status, created_at FROM hospitals WHERE is_deleted = 0
 * """);
 * 
 * MapSqlParameterSource params = new MapSqlParameterSource();
 * 
 * if (search != null && !search.isBlank()) { sql.append(""" AND ( name LIKE
 * :search OR code LIKE :search OR contact_email LIKE :search OR city LIKE
 * :search ) """); params.addValue("search", "%" + search.trim() + "%"); }
 * 
 * if (status != null && !status.isBlank()) {
 * sql.append(" AND status = :status "); params.addValue("status", status); }
 * 
 * sql.append(" ORDER BY created_at DESC ");
 * 
 * return jdbcTemplate.query(sql.toString(), params, (rs, rowNum) ->
 * mapHospital(rs)); }
 * 
 * public Optional<HospitalResponse> findById(Long id) { String sql = """ SELECT
 * id, tenant_id, name, code, contact_email, contact_phone, country, state,
 * city, pincode, timezone, subscription_plan_id, status, created_at FROM
 * hospitals WHERE id = :id AND is_deleted = 0 LIMIT 1 """;
 * 
 * try { return Optional.ofNullable(jdbcTemplate.queryForObject( sql, new
 * MapSqlParameterSource("id", id), (rs, rowNum) -> mapHospital(rs) )); } catch
 * (EmptyResultDataAccessException ex) { log.warn("Hospital not found. id={}",
 * id); return Optional.empty(); } }
 * 
 * public int updateHospital(Long id, UpdateHospitalRequest request, Long
 * updatedBy) { String sql = """ UPDATE hospitals SET name = :name,
 * contact_email = :contactEmail, contact_phone = :contactPhone, country =
 * :country, state = :state, city = :city, pincode = :pincode, timezone =
 * :timezone, subscription_plan_id = :subscriptionPlanId, updated_by =
 * :updatedBy, updated_at = NOW() WHERE id = :id AND is_deleted = 0 """;
 * 
 * return jdbcTemplate.update(sql, new MapSqlParameterSource() .addValue("id",
 * id) .addValue("name", request.getName()) .addValue("contactEmail",
 * request.getContactEmail()) .addValue("contactPhone",
 * request.getContactPhone()) .addValue("country", request.getCountry())
 * .addValue("state", request.getState()) .addValue("city", request.getCity())
 * .addValue("pincode", request.getPincode()) .addValue("timezone",
 * request.getTimezone()) .addValue("subscriptionPlanId",
 * request.getSubscriptionPlanId()) .addValue("updatedBy", updatedBy)); }
 * 
 * public int updateStatus(Long id, String status, Long updatedBy) { String sql
 * = """ UPDATE hospitals SET status = :status, updated_by = :updatedBy,
 * updated_at = NOW() WHERE id = :id AND is_deleted = 0 """;
 * 
 * return jdbcTemplate.update(sql, new MapSqlParameterSource() .addValue("id",
 * id) .addValue("status", status) .addValue("updatedBy", updatedBy)); }
 * 
 * private HospitalResponse mapHospital(java.sql.ResultSet rs) throws
 * java.sql.SQLException { return new HospitalResponse( rs.getLong("id"),
 * rs.getLong("tenant_id"), rs.getString("name"), rs.getString("code"),
 * rs.getString("contact_email"), rs.getString("contact_phone"),
 * rs.getString("country"), rs.getString("state"), rs.getString("city"),
 * rs.getString("pincode"), rs.getString("timezone"),
 * rs.getObject("subscription_plan_id") == null ? null :
 * rs.getLong("subscription_plan_id"), rs.getString("status"),
 * rs.getString("created_at") ); } }
 */

package com.plasmit.superadmin.repository;

import com.plasmit.superadmin.dto.request.CreateHospitalRequest;
import com.plasmit.superadmin.dto.request.UpdateHospitalRequest;
import com.plasmit.superadmin.dto.response.HospitalResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HospitalRepository {

    private static final Logger log = LoggerFactory.getLogger(HospitalRepository.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public HospitalRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long createTenant(String code, String name) {
        String sql = """
                INSERT INTO tenants 
                (
                    tenant_code, 
                    tenant_name, 
                    tenant_type, 
                    status
                )
                VALUES 
                (
                    :tenantCode, 
                    :tenantName, 
                    'HOSPITAL', 
                    'ACTIVE'
                )
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                sql,
                new MapSqlParameterSource()
                        .addValue("tenantCode", code)
                        .addValue("tenantName", name),
                keyHolder,
                new String[]{"id"}
        );

        Number key = keyHolder.getKey();
        Long tenantId = key == null ? null : key.longValue();

        log.debug("Tenant created. tenantId={}, code={}", tenantId, code);
        return tenantId;
    }

    public Long createHospital(CreateHospitalRequest request, Long tenantId, Long createdBy) {

        String sql = """
                INSERT INTO hospitals
                (
                    tenant_id,
                    hospital_name,
                    hospital_code,
                    email,
                    phone,
                    address,
                    city,
                    state,
                    country,
                    pincode,
                    subscription_id,
                    status,
                    created_by
                )
                VALUES
                (
                    :tenantId,
                    :hospitalName,
                    :hospitalCode,
                    :email,
                    :phone,
                    :address,
                    :city,
                    :state,
                    :country,
                    :pincode,
                    :subscriptionId,
                    :status,
                    :createdBy
                )
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                sql,
                new MapSqlParameterSource()
                        .addValue("tenantId", tenantId)
                        .addValue("hospitalName", request.getName())
                        .addValue("hospitalCode", request.getCode())
                        .addValue("email", request.getContactEmail())
                        .addValue("phone", request.getContactPhone())
                        .addValue("address", request.getAddress())
                        .addValue("city", request.getCity())
                        .addValue("state", request.getState())
                        .addValue("country", request.getCountry())
                        .addValue("pincode", request.getPincode())
                        .addValue("subscriptionId", request.getSubscriptionPlanId())
                        .addValue("status", request.getStatus() == null || request.getStatus().isBlank()
                                ? "ACTIVE"
                                : request.getStatus())
                        .addValue("createdBy", createdBy),
                keyHolder,
                new String[]{"id"}
        );

        Number key = keyHolder.getKey();
        Long hospitalId = key == null ? null : key.longValue();

        log.info("Hospital created. hospitalId={}, tenantId={}, code={}",
                hospitalId, tenantId, request.getCode());

        return hospitalId;
    }

    public List<HospitalResponse> findAll(String search, String status) {

        StringBuilder sql = new StringBuilder("""
                SELECT 
                    id,
                    tenant_id,
                    hospital_name AS name,
                    hospital_code AS code,
                    email AS contact_email,
                    phone AS contact_phone,
                    country,
                    state,
                    city,
                    pincode,
                    subscription_id AS subscription_plan_id,
                    status,
                    created_at
                FROM hospitals
                WHERE 1 = 1
                """);

        MapSqlParameterSource params = new MapSqlParameterSource();

        if (search != null && !search.isBlank()) {
            sql.append("""
                    AND (
                        hospital_name LIKE :search
                        OR hospital_code LIKE :search
                        OR email LIKE :search
                        OR city LIKE :search
                    )
                    """);
            params.addValue("search", "%" + search.trim() + "%");
        }

        if (status != null && !status.isBlank()) {
            sql.append(" AND status = :status ");
            params.addValue("status", status);
        }

        sql.append(" ORDER BY created_at DESC ");

        return jdbcTemplate.query(sql.toString(), params, (rs, rowNum) -> mapHospital(rs));
    }

    public Optional<HospitalResponse> findById(Long id) {

        String sql = """
                SELECT 
                    id,
                    tenant_id,
                    hospital_name AS name,
                    hospital_code AS code,
                    email AS contact_email,
                    phone AS contact_phone,
                    country,
                    state,
                    city,
                    pincode,
                    subscription_id AS subscription_plan_id,
                    status,
                    created_at
                FROM hospitals
                WHERE id = :id
                LIMIT 1
                """;

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(
                    sql,
                    new MapSqlParameterSource("id", id),
                    (rs, rowNum) -> mapHospital(rs)
            ));
        } catch (EmptyResultDataAccessException ex) {
            log.warn("Hospital not found. id={}", id);
            return Optional.empty();
        }
    }

    public int updateHospital(Long id, UpdateHospitalRequest request, Long updatedBy) {

        String sql = """
                UPDATE hospitals
                SET 
                    hospital_name = :hospitalName,
                    email = :email,
                    phone = :phone,
                    address = :address,
                    country = :country,
                    state = :state,
                    city = :city,
                    pincode = :pincode,
                    subscription_id = :subscriptionId,
                    updated_at = NOW()
                WHERE id = :id
                """;

        int rows = jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("hospitalName", request.getName())
                .addValue("email", request.getContactEmail())
                .addValue("phone", request.getContactPhone())
                .addValue("address", request.getAddress())
                .addValue("country", request.getCountry())
                .addValue("state", request.getState())
                .addValue("city", request.getCity())
                .addValue("pincode", request.getPincode())
                .addValue("subscriptionId", request.getSubscriptionPlanId()));

        log.info("Hospital update executed. id={}, rows={}, updatedBy={}", id, rows, updatedBy);

        return rows;
    }

    public int updateStatus(Long id, String status, Long updatedBy) {

        String sql = """
                UPDATE hospitals
                SET 
                    status = :status,
                    updated_at = NOW()
                WHERE id = :id
                """;

        int rows = jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("status", status)
        );

        log.info("Hospital status update executed. id={}, status={}, rows={}, updatedBy={}",
                id, status, rows, updatedBy);

        return rows;
    }

    private HospitalResponse mapHospital(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new HospitalResponse(
                rs.getLong("id"),
                rs.getLong("tenant_id"),
                rs.getString("name"),
                rs.getString("code"),
                rs.getString("contact_email"),
                rs.getString("contact_phone"),
                rs.getString("country"),
                rs.getString("state"),
                rs.getString("city"),
                rs.getString("pincode"),
                null,
                rs.getObject("subscription_plan_id") == null ? null : rs.getLong("subscription_plan_id"),
                rs.getString("status"),
                rs.getString("created_at")
        );
    }
}