package com.roadways.login.security;

import com.roadways.login.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationMs;

    public JwtService(@Value("${security.jwt.secret}") String secret, @Value("${security.jwt.expiration-ms:3600000}") long expirationMs) {
        if (secret == null || secret.length() < 32)
            throw new IllegalArgumentException("JWT secret must be at least 32 characters");
        key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    public String generate(String email, Role role) {
        Date now = new Date();
        return Jwts.builder().subject(email).claim("role", role.name()).issuedAt(now).expiration(new Date(now.getTime() + expirationMs)).signWith(key).compact();
    }

    public Claims claims(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
    }

    public String email(String token) {
        return claims(token).getSubject();
    }

    public String role(String token) {
        return claims(token).get("role", String.class);
    }

    public long remaining(String token) {
        return Math.max(1000, claims(token).getExpiration().getTime() - System.currentTimeMillis());
    }
}
