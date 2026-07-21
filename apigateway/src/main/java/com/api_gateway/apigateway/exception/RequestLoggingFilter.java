package com.api_gateway.apigateway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {
    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethod().name();
        String headers = exchange.getRequest().getHeaders().toString();

        log.info("=== REQUEST RECEIVED ===");
        log.info("Method: {}", method);
        log.info("Path: {}", path);
        log.info("Headers: {}", headers);
        log.info("Is Public Route? {}", isPublicRoute(path));

        return chain.filter(exchange);
    }

    private boolean isPublicRoute(String path) {
        return path.startsWith("/api/users/register") ||
                path.startsWith("/api/users/login") ||
                path.startsWith("/api/auth/") ||
                path.startsWith("/actuator/health") ||
                path.startsWith("/fallback/");
    }

    @Override
    public int getOrder() {
        return -1000;
    }
}