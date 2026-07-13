package com.api_gateway.apigateway.exception;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        String message = ex.getMessage();

        // Check for specific error messages
        if (message != null) {
            if (message.contains("Access denied") || message.contains("forbidden")) {
                return handleError(response, HttpStatus.FORBIDDEN, "Access Denied",
                        "You don't have permission to access this resource");
            }
            if (message.contains("missing authorization") ||
                    message.contains("un authorized") ||
                    message.contains("INVALID TOKEN") ||
                    message.contains("Invalid or expired token")) {
                return handleError(response, HttpStatus.UNAUTHORIZED, "Unauthorized",
                        "Please provide a valid JWT token");
            }
        }

        // Default to internal server error
        return handleError(response, HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error", "Something went wrong");
    }

    private Mono<Void> handleError(ServerHttpResponse response, HttpStatus status,
                                   String error, String message) {
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String errorBody = String.format(
                "{\"error\": \"%s\", \"message\": \"%s\"}",
                error, message
        );
        DataBuffer buffer = response.bufferFactory().wrap(errorBody.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}