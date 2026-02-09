package com.tmq.module_25.security;

import com.tmq.module_25.exception.AuthException;
import com.tmq.module_25.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;

@AllArgsConstructor
public class JwtHandler {

    private final String secret;

    public static class VerificationResult {
        public Claims claims;
        public String token;

        public VerificationResult(String token, Claims claims) {
            this.token = token;
            this.claims = claims;
        }
    }

    public Mono<VerificationResult> check (String accessToken) {
        return Mono.just(verify(accessToken))
                .onErrorResume(e -> Mono.error(new UnauthorizedException(e.getMessage())));
    }

    private VerificationResult verify (String token) {
        Claims claims = getClaimsFromToken(token);
        final Date expiration = claims.getExpiration();

        if (expiration.before(new Date())) {
            throw new RuntimeException("Token expired");
        }

        return new VerificationResult(token, claims);
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encode(secret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }
}
