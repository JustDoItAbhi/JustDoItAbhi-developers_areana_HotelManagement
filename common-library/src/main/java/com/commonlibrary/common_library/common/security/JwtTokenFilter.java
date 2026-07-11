//package com.commonlibrary.common_library.common.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.crypto.SecretKey;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//@Component
//@Slf4j
//public class JwtTokenFilter extends OncePerRequestFilter {
//
//    @Value("${spring.jwt.secretkey:ThisIsMySuperSecretJwtKeyForAuthentication2026Secure}")
//    private String jwtSecretKey;
//
//    private SecretKey getSecretKey() {
//        byte[] keyBytes = jwtSecretKey.getBytes(StandardCharsets.UTF_8);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = authHeader.substring(7);
//        try {
//            Claims claims = Jwts.parser()
//                    .verifyWith(getSecretKey())
//                    .build()
//                    .parseSignedClaims(token)
//                    .getPayload();
//
//            String email = claims.getSubject();
//            String role = claims.get("role", String.class);
//
//            if (email != null && role != null) {
//                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                        email, null, List.of(authority)
//                );
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//                log.debug("Authenticated user: {} with role: {}", email, role);
//            }
//        } catch (Exception e) {
//            log.error("JWT token verification failed: {}", e.getMessage());
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
