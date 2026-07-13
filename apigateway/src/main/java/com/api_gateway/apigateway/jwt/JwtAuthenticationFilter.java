//package com.api_gateway.apigateway.jwt;
//
//import com.api_gateway.apigateway.jwt.JwtUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.util.AntPathMatcher;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//@Component
//public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
//
//    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
//
//    private RouteValidator vlidator;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Value("${app.jwt.public-routes:}")
//    private List<String> publicRoutes;
//
//    private final AntPathMatcher pathMatcher = new AntPathMatcher();
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        String path = request.getPath().value();
//        String method = request.getMethod().name();
//
//        log.info("=== JWT FILTER PROCESSING ===");
//        log.info("Path: {}", path);
//        log.info("Method: {}", method);
//        log.info("Public Routes Config: {}", publicRoutes);
//
//        // Check if the path is public
//        boolean isPublic = isPublicRoute(path);
//        log.info("Is Public Route: {}", isPublic);
//
//        if (isPublic) {
//            log.info("✅ PUBLIC ROUTE ALLOWED: {}", path);
//            return chain.filter(exchange);
//        }
//
//        log.info("🔒 PROTECTED ROUTE: {}", path);
//
//        // Get Authorization header
//        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
//        log.info("Authorization Header: {}", authHeader != null ? "Present" : "Missing");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            log.warn("❌ Missing or invalid Authorization header for protected route: {}", path);
//            return handleUnauthorized(exchange, "Missing or invalid Authorization header");
//        }
//
//        String token = authHeader.substring(7);
//        log.info("Token extracted (first 20 chars): {}...", token.substring(0, Math.min(20, token.length())));
//
//        boolean isValid = jwtUtil.validateToken(token);
//        log.info("Token Valid: {}", isValid);
//
//        if (!isValid) {
//            log.warn("❌ Invalid or expired token for: {}", path);
//            return handleUnauthorized(exchange, "Invalid or expired token");
//        }
//
//    }
//
//    private boolean isPublicRoute(String path) {
//        // Always allow these paths regardless of configuration
//        if (path.startsWith("/api/user/register") ||
//                path.startsWith("/api/user/login") ||
//                path.startsWith("/actuator/health") ||
//                path.startsWith("/fallback/")) {
//            log.debug("Path matched default public route: {}", path);
//            return true;
//        }
//
//        // Check configured public routes
//        if (publicRoutes != null && !publicRoutes.isEmpty()) {
//            boolean matched = publicRoutes.stream()
//                    .anyMatch(pattern -> {
//                        boolean match = pathMatcher.match(pattern, path);
//                        if (match) {
//                            log.debug("Path matched configured public route: {} pattern: {}", path, pattern);
//                        }
//                        return match;
//                    });
//
//            if (matched) {
//                return true;
//            }
//        }
//
//        // Allow OPTIONS requests (CORS preflight)
//        return false;
//    }
//
//    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message) {
//        ServerHttpResponse response = exchange.getResponse();
//        response.setStatusCode(HttpStatus.UNAUTHORIZED);
//        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
//
//        String body = String.format(
//                "{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\"}",
//                java.time.Instant.now(),
//                HttpStatus.UNAUTHORIZED.value(),
//                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
//                message
//        );
//
//        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
//        return response.writeWith(Mono.just(buffer));
//    }
//
//    @Override
//    public int getOrder() {
//        return -1;
//    }
//}