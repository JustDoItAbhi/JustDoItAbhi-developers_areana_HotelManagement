package com.api_gateway.apigateway.jwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;


@Component
public class JwtUtil {
    @Value("${spring.jwt.secretkey}")
    private String jwtSecretKey;
    @Value("${spring.jwt.expiry}")
    private long jwtExpiry;
    private SecretKey getSecretKey() {
        byte[] keyByte = this.jwtSecretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public boolean validateToken(final String token) {
        System.out.println("TOCKEN  "+token);
        try {
          Jwts.parser()
                  .setSigningKey(getSecretKey())
                  .build()
                  .parseClaimsJws(token);
          return true;
      }catch (Exception e){
          System.out.println("TOKEN INVALID "+token);
          throw new RuntimeException("INVALID TOKEN ");
      }
    }



    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}