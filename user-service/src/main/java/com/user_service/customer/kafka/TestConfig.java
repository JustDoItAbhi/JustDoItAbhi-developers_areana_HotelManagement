package com.user_service.customer.kafka;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class TestConfig {

    @Bean
    public CommandLineRunner test(JavaMailSender sender) {
        return args -> System.out.println("JavaMailSender bean = " + sender);
    }
}