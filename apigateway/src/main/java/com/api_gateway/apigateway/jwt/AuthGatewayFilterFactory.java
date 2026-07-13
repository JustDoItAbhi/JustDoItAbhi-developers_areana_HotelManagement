//package com.api_gateway.apigateway.jwt;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//
//import java.util.List;
//
//@Component
//public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthGatewayFilterFactory.Config> {
//
//    public AuthGatewayFilterFactory() {
//        super(Config.class);
//    }
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return (exchange, chain) -> {
//            String requiredRole = config.getRequiredRole();
//            if (requiredRole != null && !requiredRole.isEmpty()) {
//                String rolesHeader = exchange.getRequest().getHeaders().getFirst("X-User-Roles");
//                if (rolesHeader == null || !rolesHeader.contains(requiredRole)) {
//                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
//                    return exchange.getResponse().setComplete();
//                }
//            }
//            return chain.filter(exchange);
//        };
//    }
//
//    public static class Config {
//        private String requiredRole;
//
//        public String getRequiredRole() {
//            return requiredRole;
//        }
//
//        public void setRequiredRole(String requiredRole) {
//            this.requiredRole = requiredRole;
//        }
//    }
//}