package com.api_gateway.apigateway.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private final RouteValidator validator;

    @Autowired
    private final JwtUtil jwtUtil;

    public AuthenticationFilter(RouteValidator validator, JwtUtil jwtUtil) {
        super(Config.class);
        this.validator = validator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String name() {
        return "Authentication";
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            System.out.println("Authentication Filter - Path: " + path);

            if (validator.isPublic.test(exchange.getRequest())) {
                System.out.println("Public endpoint - skipping authentication");
                return chain.filter(exchange);
            }

            System.out.println("Protected endpoint - validating token and roles");

            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing authorization header");
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid authorization header format");
            }

            try {
                jwtUtil.validateToken(authHeader);
                System.out.println("Token validated successfully");

                //  roles from token
                List<String> userRoles = jwtUtil.extractRoles(authHeader);
                System.out.println("User roles: " + userRoles);

                // user access
                if (!validator.hasAccess(path, userRoles)) {
                    System.out.println(" Access denied for roles: " + userRoles + " to path: " + path);
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied for this endpoint");
                }

                System.out.println("Access granted for roles: " + userRoles);

                // Add user info to headers for downstream services
                String userId = jwtUtil.extractUserId(authHeader);
                String email = jwtUtil.extractEmail(authHeader);

                exchange = exchange.mutate()
                        .request(r -> r.header("X-User-Id", userId)
                                .header("X-User-Email", email)
                                .header("X-User-Roles", String.join(",", userRoles)))
                        .build();

            } catch (ResponseStatusException e) {
                throw e;
            } catch (Exception e) {
                System.out.println("Error validating token: " + e.getMessage());
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
            }

            return chain.filter(exchange);
        });
    }

    public static class Config {
    }
}