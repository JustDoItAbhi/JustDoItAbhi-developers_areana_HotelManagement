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
import java.util.Map;

@Component
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        String message = ex.getMessage();

        // Get method name and path
        String method = exchange.getRequest().getMethod().name();
        String path = exchange.getRequest().getPath().value();
        String queryParams = exchange.getRequest().getQueryParams().toString();

        // Get exception details
        String exceptionName = ex.getClass().getSimpleName();
        StackTraceElement[] stackTrace = ex.getStackTrace();
        String methodName = stackTrace.length > 0 ? stackTrace[0].getMethodName() : "Unknown";
        String className = stackTrace.length > 0 ? stackTrace[0].getClassName() : "Unknown";
        int lineNumber = stackTrace.length > 0 ? stackTrace[0].getLineNumber() : 0;

        // Log detailed error information
        System.err.println("=== ERROR DETAILS ===");
        System.err.println("Path: " + path);
        System.err.println("Method: " + method);
        System.err.println("Query Params: " + queryParams);
        System.err.println("Exception: " + exceptionName);
        System.err.println("Message: " + message);
        System.err.println("Class: " + className);
        System.err.println("Method: " + methodName);
        System.err.println("Line: " + lineNumber);
        System.err.println("====================");

        // Check for specific error messages
        if (message != null) {
            if (message.contains("Access denied") || message.contains("forbidden")) {
                return handleError(response, HttpStatus.FORBIDDEN, "Access Denied",
                        "You don't have permission to access this resource", method, path);
            }
            if (message.contains("missing authorization") ||
                    message.contains("un authorized") ||
                    message.contains("INVALID TOKEN") ||
                    message.contains("Invalid or expired token")) {
                return handleError(response, HttpStatus.UNAUTHORIZED, "Unauthorized",
                        "Please provide a valid JWT token", method, path);
            }
        }

        // Default to internal server error
        return handleError(response, HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error", "Something went wrong: " + message, method, path);
    }

    private Mono<Void> handleError(ServerHttpResponse response, HttpStatus status,
                                   String error, String message, String method, String path) {
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String errorBody = String.format(
                "{\"error\": \"%s\", \"message\": \"%s\", \"method\": \"%s\", \"path\": \"%s\", \"timestamp\": \"%s\"}",
                error, message, method, path, java.time.LocalDateTime.now()
        );
        DataBuffer buffer = response.bufferFactory().wrap(errorBody.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}