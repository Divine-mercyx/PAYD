package org.payd.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils {

    private final String secretKey = "abskasvbakjsbdvjad9671231j2eiu1u2gei12e";

    private final long ACCESS_TOKEN_EXPIRY = 900_000;    // 15 Minutes
    private final long REFRESH_TOKEN_EXPIRY = 604_800_000; // 7 Days
    private final long FORGET_PASSWORD_EXPIRY = 900_000;    // 15 Minutes

    // --- GENERATION ---

    public Map<String, String> generateTokenPair(Long userId) {
        // Convert Long ID to String for the JWT Subject
        String sub = String.valueOf(userId);

        String accessToken = createToken(new HashMap<>(), sub, ACCESS_TOKEN_EXPIRY);
        String refreshToken = createToken(new HashMap<>(), sub, REFRESH_TOKEN_EXPIRY);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }


    public String generateEmailToken(String email) {
        return createToken(new HashMap<>(), email, FORGET_PASSWORD_EXPIRY);
    }

    private String createToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    // --- EXTRACTION & VALIDATION ---

    /**
     * Extracts the User ID from the token
     */
    public Long extractUserId(String token) {
        String idString = extractClaim(token, Claims::getSubject);
        return Long.parseLong(idString);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token) {
        try {
            return !extractClaim(token, Claims::getExpiration).before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
