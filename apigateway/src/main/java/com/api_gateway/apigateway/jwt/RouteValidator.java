package com.api_gateway.apigateway.jwt;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/users/register",
            "/api/users/login",
            "/api/auth",
            "/actuator/health",
            "/fallback",
            "/api/bookings/bookingResult/"
    );

    // Role-based access control mappings
    public static final Map<String, List<String>> ROLE_ENDPOINT_MAPPING = Map.of(
            // Admin-only endpoints
            "ROLE_ADMIN", List.of(
                    "/api/users/getallusers",
                    "/api/admin/**",
                    "/api/users/**",
                    "/api/hotels/**",
                    "/api/bookings/**",
                    "/api/payments/create"

            ),

            // Hotel Manager endpoints
            "ROLE_MANAGER", List.of(
                    "/api/hotels/**",
                    "/api/hotels/getallhotels",
                    "/api/hotels/**/update",
                    "/api/hotels/**/delete",
                    "/api/bookings/**",
                    "/api/payments/create"
            ),

            // User endpoints (accessible by all authenticated users)
            "ROLE_USER", List.of(
                    "/api/users/{email}",
                    "/api/bookings/**",
                    "/api/hotels/getallhotels",
                    "/api/payments/create",
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