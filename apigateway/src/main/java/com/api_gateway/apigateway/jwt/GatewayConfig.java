//package com.api_gateway.apigateway.jwt;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//
//@Configuration
//@EnableWebSecurity
//public class GatewayConfig {
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers("/api/user/register").permitAll()
//                        .requestMatchers("/api/user/login").permitAll()
//                        .anyRequest().authenticated()
//                );
//        // Form login handles the redirect to the login page from the
//        // authorization server filter chain
////                .formLogin(Customizer.withDefaults());
//
//        return http.build();
//
//    }
//}
////    @Bean
////    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
////        return builder.routes()
////                // User Service - Public routes (no auth)
////                .route("user-service-public", r -> r
////                        .path("/api/user/register", "/api/user/login")
////                        .filters(f -> f.stripPrefix(0))
////                        .uri("lb://http://localhost:8081"))
////
////                // User Service - Protected routes (with auth)
////                .route("user-service-protected", r -> r
////                        .path("/api/user/**")
////                        .filters(f -> f.stripPrefix(0))
////                        .uri("lb://http://localhost:8081"))
////
////                // Other services
////                .route("hotel-service", r -> r
////                        .path("/api/hotel/**")
////                        .uri("lb://hotel-service"))
////
////                .route("booking-service", r -> r
////                        .path("/api/booking/**")
////                        .uri("lb://booking-service"))
////
////                .route("payment-service", r -> r
////                        .path("/pay/**")
////                        .uri("lb://payment-service"))
////
////                .build();
////    }
////}
