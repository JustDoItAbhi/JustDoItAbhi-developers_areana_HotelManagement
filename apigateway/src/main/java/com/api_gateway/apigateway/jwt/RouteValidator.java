package com.api_gateway.apigateway.jwt;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    // Public endpoints
    public static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/user/register",
            "/api/user/login",
            "/api/auth",
            "/actuator/health",
            "/fallback"
    );

    // Role-based access control mappings
    public static final Map<String, List<String>> ROLE_ENDPOINT_MAPPING = Map.of(
            // Admin-only endpoints
            "ROLE_ADMIN", List.of(
                    "/api/user/getallusers",
                    "/api/admin/**",
                    "/api/user/**",
                    "/api/hotel/**/delete",
                    "/api/booking/**/delete"
            ),

            // Hotel Manager endpoints
            "ROLE_MANAGER", List.of(
                    "/api/hotel/**",
                    "/api/hotel/getallhotels",
                    "/api/hotel/**/update",
                    "/api/hotel/**/delete",
                    "/api/booking/**"
            ),

            // User endpoints (accessible by all authenticated users)
            "ROLE_USER", List.of(
                    "/api/user/**",
                    "/api/booking/**",
                    "/api/hotel/getallhotels",
                    "/api/pay/**",
                    "/api/inventory/**",
                    "/api/location/**",
                    "/api/notification/**"
            )
    );

    public Predicate<ServerHttpRequest> isPublic = request -> {
        String path = request.getURI().getPath();
        return PUBLIC_ENDPOINTS.stream().anyMatch(path::contains);
    };
    public boolean hasAccess(String path, List<String> userRoles) {
        for (String role : userRoles) {
            List<String> allowedPaths = ROLE_ENDPOINT_MAPPING.get(role);
            if (allowedPaths != null) {
                for (String pattern : allowedPaths) {
                    if (path.matches(pattern.replace("**", ".*"))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}