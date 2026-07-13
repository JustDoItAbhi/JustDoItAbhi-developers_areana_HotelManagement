//package com.api_gateway.apigateway.controller;
//
//
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import reactor.core.publisher.Mono;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/fallback")
//public class FallbackController {
//
//    @GetMapping("/user")
//    public Mono<Map<String, String>> userServiceFallback() {
//        return Mono.just(Map.of(
//                "status", "503",
//                "message", "User service is currently unavailable. Please try again later."
//        ));
//    }
//
//    @GetMapping("/hotel")
//    public Mono<Map<String, String>> hotelServiceFallback() {
//        return Mono.just(Map.of(
//                "status", "503",
//                "message", "Hotel service is currently unavailable. Please try again later."
//        ));
//    }
//
//    @GetMapping("/booking")
//    public Mono<Map<String, String>> bookingServiceFallback() {
//        return Mono.just(Map.of(
//                "status", "503",
//                "message", "Booking service is currently unavailable. Please try again later."
//        ));
//    }
//
//    @GetMapping("/pay")
//    public Mono<Map<String, String>> paymentServiceFallback() {
//        return Mono.just(Map.of(
//                "status", "503",
//                "message", "Payment service is currently unavailable. Please try again later."
//        ));
//    }
//}