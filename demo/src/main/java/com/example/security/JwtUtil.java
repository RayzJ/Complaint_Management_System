package com.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    private final Key signingKey;

    public JwtUtil(@org.springframework.beans.factory.annotation.Value("${app.jwt.secret}") String secret) {
        if (secret == null || secret.isBlank() || "ChangeThisToASecureSecretInProduction!".equals(secret)) {
            logger.error(
                    "JWT secret not configured securely. Please set app.jwt.secret in application.properties or JWT_SECRET environment variable");
            throw new IllegalStateException("JWT secret not configured securely");
        }

        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-512");
            keyBytes = sha.digest(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 algorithm not available", e);
        }

        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Method to generate JWT token
    public String generateToken(String username, String fullName, List<String> roles, Integer userId) {
        return createToken(username, fullName, roles, userId);
    }

    private String createToken(String subject, String fullName, List<String> roles, Integer userId) {
        long validity = 1000L * 60 * 60 * 10; // 10 hours
        return Jwts.builder()
                .setSubject(subject)
                .claim("fullName", fullName)
                .claim("roles", roles)
                .claim("userId", userId) // Add userId as a claim
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validity))
                .signWith(signingKey)
                .compact();
    }

    public Boolean validateToken(String token, String username) {
        final String extracted = extractUsername(token);
        return (extracted.equals(username) && !isTokenExpired(token));
    }

    // Extract the userId from the token
    public Integer extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Integer.class);  // Extracting userId from claims
    }

    // Extract the role from the token
    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class).get(0).toString();  // Assuming role is stored as a list
    }
}
