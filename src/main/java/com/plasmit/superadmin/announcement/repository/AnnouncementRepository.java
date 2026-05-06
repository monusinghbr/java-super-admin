package com.plasmit.superadmin.announcement.repository;

import com.plasmit.superadmin.announcement.dto.request.CreateAnnouncementRequest;
import com.plasmit.superadmin.announcement.dto.response.AnnouncementResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnnouncementRepository {

    private static final Logger log = LoggerFactory.getLogger(AnnouncementRepository.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AnnouncementRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long createAnnouncement(CreateAnnouncementRequest request, Long createdBy) {
        String sql = """
                INSERT INTO announcements
                (title, body, audience, status, scheduled_at, created_by)
                VALUES
                (:title, :body, :audience, :status, :scheduledAt, :createdBy)
                """;

        String status = request.getScheduledAt() == null || request.getScheduledAt().isBlank()
                ? "DRAFT"
                : "SCHEDULED";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("title", request.getTitle())
                .addValue("body", request.getBody())
                .addValue("audience", request.getAudience())
                .addValue("status", status)
                .addValue("scheduledAt", request.getScheduledAt())
                .addValue("createdBy", createdBy), keyHolder);

        Long id = keyHolder.getKey() == null ? null : keyHolder.getKey().longValue();

        log.debug("Announcement inserted. id={}, audience={}", id, request.getAudience());

        return id;
    }

    public void addHospitalTargets(Long announcementId, List<Long> hospitalIds) {
        if (hospitalIds == null || hospitalIds.isEmpty()) {
            return;
        }

        String sql = """
                INSERT INTO announcement_hospitals
                (announcement_id, hospital_id, tenant_id)
                SELECT :announcementId, h.id, h.tenant_id
                FROM hospitals h
                WHERE h.id = :hospitalId
                  AND h.is_deleted = 0
                """;

        for (Long hospitalId : hospitalIds) {
            jdbcTemplate.update(sql, new MapSqlParameterSource()
                    .addValue("announcementId", announcementId)
                    .addValue("hospitalId", hospitalId));
        }

        log.debug("Hospital targets added. announcementId={}, count={}",
                announcementId, hospitalIds.size());
    }

    public List<AnnouncementResponse> findAnnouncements(String status, String audience) {
        StringBuilder sql = new StringBuilder("""
                SELECT id, title, body, audience, status,
                       scheduled_at, broadcasted_at, created_at
                FROM announcements
                WHERE is_deleted = 0
                """);

        MapSqlParameterSource params = new MapSqlParameterSource();

        if (status != null && !status.isBlank()) {
            sql.append(" AND status = :status ");
            params.addValue("status", status);
        }

        if (audience != null && !audience.isBlank()) {
            sql.append(" AND audience = :audience ");
            params.addValue("audience", audience);
        }

        sql.append(" ORDER BY created_at DESC ");

        return jdbcTemplate.query(sql.toString(), params, (rs, rowNum) ->
                new AnnouncementResponse(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("body"),
                        rs.getString("audience"),
                        rs.getString("status"),
                        rs.getString("scheduled_at"),
                        rs.getString("broadcasted_at"),
                        rs.getString("created_at")
                ));
    }

    public int broadcastAnnouncement(Long announcementId, Long updatedBy) {
        String sql = """
                UPDATE announcements
                SET status = 'BROADCASTING',
                    broadcasted_at = NOW(),
                    updated_by = :updatedBy,
                    updated_at = NOW()
                WHERE id = :announcementId
                  AND is_deleted = 0
                  AND status IN ('DRAFT', 'SCHEDULED')
                """;

        int updated = jdbcTemplate.update(sql, new MapSqlParameterSource()
                .addValue("announcementId", announcementId)
                .addValue("updatedBy", updatedBy));

        log.debug("Broadcast announcement updated. announcementId={}, affectedRows={}",
                announcementId, updated);

        return updated;
    }

    public boolean existsById(Long announcementId) {
        String sql = """
                SELECT COUNT(*)
                FROM announcements
                WHERE id = :announcementId
                  AND is_deleted = 0
                """;

        Integer count = jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource("announcementId", announcementId),
                Integer.class);

        return count != null && count > 0;
    }
}