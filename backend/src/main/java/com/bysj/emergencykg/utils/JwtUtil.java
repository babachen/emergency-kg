package com.bysj.emergencykg.utils;

import com.bysj.emergencykg.config.AppProperties;
import com.bysj.emergencykg.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final AppProperties appProperties;
    public JwtUtil(AppProperties appProperties) {
        this.appProperties = appProperties;
        this.secretKey = Keys.hmacShaKeyFor(appProperties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8));
    }
    public String generateToken(Long userId, String username) {
        LocalDateTime now = LocalDateTime.now();
        return Jwts.builder().claim("userId", userId).claim("username", username).issuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant())).expiration(Date.from(now.plusHours(appProperties.getJwt().getExpireHours()).atZone(ZoneId.systemDefault()).toInstant())).signWith(secretKey).compact();
    }
    public Long parseUserId(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
            return Long.parseLong(String.valueOf(claims.get("userId")));
        } catch (Exception e) {
            throw new BusinessException(401, "登录已过期，请重新登录");
        }
    }
}
