package com.user_service.customer.jwtservice;

import com.user_service.customer.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {
    @Value("${spring.jwt.secretkey}")
    private String jwtSecretKey;
    @Value("${spring.jwt.expiry}")
    private long jwtExpiry;

    private SecretKey getSecretKey() {
        byte[] keyByte = this.jwtSecretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public String generateJwtToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpiry);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId().toString());
        claims.put("roles", List.of("ROLE_" + user.getRole().name()));
        claims.put("email", user.getEmail());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSecretKey())
                .compact();
    }
}