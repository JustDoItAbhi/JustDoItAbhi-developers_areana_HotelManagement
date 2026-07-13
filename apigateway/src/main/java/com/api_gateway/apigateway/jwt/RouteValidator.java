package com.api_gateway.apigateway.jwt;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/user/register",
            "/api/user/login",
            "/api/auth",
            "/actuator/health",
            "/fallback"
    );

    public Predicate<ServerHttpRequest> isSecured = request -> {
        String path = request.getURI().getPath();
        return PUBLIC_ENDPOINTS.stream().noneMatch(path::contains);
    };
}