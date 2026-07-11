package com.booking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
        (    scanBasePackages = {
                "com.booking_service",
                "com.commonlibrary.common_library"
        })
@EnableFeignClients
@EnableDiscoveryClient
@org.springframework.scheduling.annotation.EnableScheduling
public class BookingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingServiceApplication.class, args);
	}

}
