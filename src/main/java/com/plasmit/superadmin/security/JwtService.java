package com.plasmit.superadmin.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (Exception ex) {
            log.warn("Invalid JWT token: {}", ex.getMessage());
            return false;
        }
    }

    public Long extractUserId(String token) {
        return Long.valueOf(extractClaims(token).getSubject());
    }

    public Long extractTenantId(String token) {
        Object tenantId = extractClaims(token).get("tenantId");

        if (tenantId == null) {
            return null;
        }

        if (tenantId instanceof Integer) {
            return ((Integer) tenantId).longValue();
        }

        if (tenantId instanceof Long) {
            return (Long) tenantId;
        }

        return Long.valueOf(String.valueOf(tenantId));
    }

    public String extractEmail(String token) {
        return extractClaims(token).get("email", String.class);
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    public String extractUserType(String token) {
        return extractClaims(token).get("userType", String.class);
    }

    public Boolean extractImpersonated(String token) {
        Boolean value = extractClaims(token).get("impersonated", Boolean.class);
        return value != null && value;
    }

    public Long extractImpersonatedBy(String token) {
        Object value = extractClaims(token).get("impersonatedBy");

        if (value == null) {
            return null;
        }

        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        }

        if (value instanceof Long) {
            return (Long) value;
        }

        return Long.valueOf(String.valueOf(value));
    }

    public Long extractHospitalId(String token) {
        Object value = extractClaims(token).get("hospitalId");

        if (value == null) {
            return null;
        }

        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        }

        if (value instanceof Long) {
            return (Long) value;
        }

        return Long.valueOf(String.valueOf(value));
    }

    public String generateImpersonationToken(Long adminId,
                                             Long hospitalId,
                                             Long tenantId) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (10 * 60 * 1000)); // 10 minutes

        return Jwts.builder()
                .subject(String.valueOf(adminId))
                .claim("email", null)
                .claim("role", "HOSPITAL_ADMIN")
                .claim("userType", "HOSPITAL_USER")
                .claim("tenantId", tenantId)
                .claim("hospitalId", hospitalId)
                .claim("impersonated", true)
                .claim("impersonatedBy", adminId)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}