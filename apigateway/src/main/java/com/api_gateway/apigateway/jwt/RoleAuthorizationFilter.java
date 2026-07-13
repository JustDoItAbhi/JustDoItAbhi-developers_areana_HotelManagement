//package com.api_gateway.apigateway.jwt;
//
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class RoleAuthorizationFilter implements GlobalFilter, Ordered {
//
//    private final Map<String, List<String>> rolePermissions = Map.of(
//            "/api/hotel/**", List.of("ADMIN", "MANAGER"),
//            "/api/room/**", List.of("ADMIN", "MANAGER"),
//            "/api/booking/**", List.of("USER", "ADMIN"),
//            "/api/pay/**", List.of("USER", "ADMIN"),
//            "/api/user/**", List.of("USER", "ADMIN", "MANAGER")
//    );
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//
//        ServerHttpRequest request = exchange.getRequest();
//        String path = request.getPath().value();
//        System.out.println("Role Filter Path = " + path);
//        System.out.println("Public = " + isPublicRoute(path));
//
//        String rolesHeader = request.getHeaders().getFirst("X-User-Roles");
//        System.out.println("Roles = " + rolesHeader);
//        // Skip authorization for public routes
//        if (isPublicRoute(path)) {
//            return chain.filter(exchange);
//        }
//
//        // Get user roles from headers (set by JwtAuthenticationFilter)
//        String rolesHeaders = request.getHeaders().getFirst("X-User-Roles");
//        if (rolesHeaders == null || rolesHeaders.isEmpty()) {
//            return handleForbidden(exchange, "User roles not found");
//        }
//
//        List<String> userRoles = Arrays.asList(rolesHeader.split(","));
//
//        // Check if user has required role for this path
//        boolean hasAccess = rolePermissions.entrySet().stream()
//                .filter(entry -> path.matches(entry.getKey().replace("**", ".*")))
//                .findFirst()
//                .map(entry -> entry.getValue().stream().anyMatch(userRoles::contains))
//                .orElse(true); // Allow access if no specific permission defined
//
//        if (!hasAccess) {
//            return handleForbidden(exchange, "Insufficient permissions");
//        }
//
//        return chain.filter(exchange);
//    }
//
//    private boolean isPublicRoute(String path) {
//        return path.startsWith("/api/user/login") ||
//                path.startsWith("/api/user/register") ||
//                path.startsWith("/api/auth/") ||
//                path.startsWith("/actuator/") ||
//                path.startsWith("/fallback/");
//    }
//
//    private Mono<Void> handleForbidden(ServerWebExchange exchange, String message) {
//        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
//        String body = String.format("{\"error\": \"Forbidden\", \"message\": \"%s\"}", message);
//        return exchange.getResponse().writeWith(
//                Mono.just(exchange.getResponse().bufferFactory().wrap(body.getBytes()))
//        );
//    }
//
//    @Override
//    public int getOrder() {
//        return 0; // Run after JwtAuthenticationFilter
//    }
//}