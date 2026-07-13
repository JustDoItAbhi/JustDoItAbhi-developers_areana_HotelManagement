package com.api_gateway.apigateway.jwt;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebugConfig {

    @Bean
    CommandLineRunner runner(ApplicationContext context) {
        return args -> {
            System.out.println("===== AuthenticationFilter =====");
            System.out.println(context.containsBean("authenticationFilter"));
        };
    }
}
