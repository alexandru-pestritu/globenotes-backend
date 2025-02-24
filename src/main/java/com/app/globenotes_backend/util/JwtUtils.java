package com.app.globenotes_backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expiration-ms:3600000}")
    private long accessExpirationMs;

    public String generateAccessToken(Long userId, String email) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + accessExpirationMs;

        return JWT.create()
                .withClaim("userId", userId)
                .withSubject(email)
                .withIssuedAt(new Date(nowMillis))
                .withExpiresAt(new Date(expMillis))
                .sign(algorithm);
    }

    public boolean isTokenValid(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        DecodedJWT decoded = decodeToken(token);
        return decoded.getClaim("userId").asLong();
    }

    public String getEmailFromToken(String token) {
        DecodedJWT decoded = decodeToken(token);
        return decoded.getSubject();
    }

    private DecodedJWT decodeToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public String generateRefreshTokenString() {
        return UUID.randomUUID().toString();
    }
}