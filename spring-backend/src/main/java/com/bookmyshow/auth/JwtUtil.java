package com.bookmyshow.auth;

import com.bookmyshow.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.io.Decoders;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final AppProperties appProperties;
    private final SecretKey key;

    public JwtUtil(AppProperties appProperties) {
        this.appProperties = appProperties;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(appProperties.getJwtSecret()));
    }

    public String generateToken(Long userId, String name) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim("userId", userId)
                .claim("name", name)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + appProperties.getJwtExpirationMs()))
                .signWith(key)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        Object userId = claims.get("userId");
        if (userId instanceof Number) {
            return ((Number) userId).longValue();
        }
        if (userId instanceof String) {
            return Long.parseLong((String) userId);
        }
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
