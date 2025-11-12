package com.tmq.util;

import com.tmq.exception.AuthException;
import com.tmq.exception.GeneralException;
import com.tmq.model.Role;
import io.jsonwebtoken.*;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@UtilityClass
public class JwtUtil {
    public static String createToken(Integer userId, String userType) {
        return Jwts.builder()
                .header().add(Map.of("typ", "JWT")).add(Map.of("alg", "HS256")).add(Map.of("mid", userId))
                .and()
                .claims().add(Map.of("role", userType))
                .and()
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(600)))
                .issuer("tmq.com")
                .signWith(SignatureAlgorithm.HS256, PropertiesUtil.get("jwt.token"))
                .compact();

    }

    public static boolean validateToken(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .setSigningKey(PropertiesUtil.get("jwt.token"))
                    .build();
            if (token.startsWith("Bearer ")) token = token.substring(7);
            Claims claims = parser.parseClaimsJws(token).getBody();
            if (claims.getExpiration().before(new Date())) return false;
            if (!claims.getIssuer().equals("tmq.com")) return false;
            return true;
        } catch (RuntimeException e) {
            throw new AuthException("Пользователь не авторизован");
        }
    }

    public static Integer getUserId(String token) {
        validateToken(token);
        if (token.startsWith("Bearer ")) token = token.substring(7);
        String id = Jwts.parser().setSigningKey(PropertiesUtil.get("jwt.token")).build().parseClaimsJws(token).getHeader().get("mid").toString();
        return Integer.valueOf(id);
    }

    public static Role getUserRole(String token) {
        validateToken(token);
        if (token.startsWith("Bearer ")) token = token.substring(7);
        String role = Jwts.parser().setSigningKey(PropertiesUtil.get("jwt.token"))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role").toString();
        return Role.valueOf(role);
    }
}
