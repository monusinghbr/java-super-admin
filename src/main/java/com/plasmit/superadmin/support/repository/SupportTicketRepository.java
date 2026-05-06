package com.plasmit.superadmin.support.repository;

import com.plasmit.superadmin.support.dto.response.SupportTicketResponse;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SupportTicketRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public SupportTicketRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<SupportTicketResponse> findTickets(String status,
                                                   Long hospitalId) {

        StringBuilder sql = new StringBuilder("""
                SELECT id,
                       ticket_code,
                       hospital_id,
                       subject,
                       priority,
                       status,
                       owner_name,
                       created_at
                FROM support_tickets
                WHERE is_deleted = 0
                """);

        MapSqlParameterSource params = new MapSqlParameterSource();

        if (status != null) {
            sql.append(" AND status = :status");
            params.addValue("status", status);
        }

        if (hospitalId != null) {
            sql.append(" AND hospital_id = :hospitalId");
            params.addValue("hospitalId", hospitalId);
        }

        sql.append(" ORDER BY created_at DESC");

        return jdbc.query(sql.toString(),
                params,
                (rs, i) -> new SupportTicketResponse(
                        rs.getLong("id"),
                        rs.getString("ticket_code"),
                        rs.getLong("hospital_id"),
                        rs.getString("subject"),
                        rs.getString("priority"),
                        rs.getString("status"),
                        rs.getString("owner_name"),
                        rs.getString("created_at")
                ));
    }

    public void updateStatus(Long ticketId,
                             String newStatus,
                             Long userId) {

        jdbc.update("""
                UPDATE support_tickets
                SET status = :status,
                    updated_at = NOW()
                WHERE id = :ticketId
                """,
                new MapSqlParameterSource()
                        .addValue("status", newStatus)
                        .addValue("ticketId", ticketId));

        jdbc.update("""
                INSERT INTO support_ticket_status_history
                (ticket_id, new_status, changed_by)
                VALUES (:ticketId, :status, :userId)
                """,
                new MapSqlParameterSource()
                        .addValue("ticketId", ticketId)
                        .addValue("status", newStatus)
                        .addValue("userId", userId));
    }

    public void assignTicket(Long ticketId,
                             Long ownerId,
                             String ownerName) {

        jdbc.update("""
                UPDATE support_tickets
                SET owner_user_id = :ownerId,
                    owner_name = :ownerName,
                    updated_at = NOW()
                WHERE id = :ticketId
                """,
                new MapSqlParameterSource()
                        .addValue("ownerId", ownerId)
                        .addValue("ownerName", ownerName)
                        .addValue("ticketId", ticketId));
    }
}