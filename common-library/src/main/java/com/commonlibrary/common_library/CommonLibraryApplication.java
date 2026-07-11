package com.commonlibrary.common_library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableDiscoveryClient
@EnableKafka
@EnableFeignClients
public class CommonLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonLibraryApplication.class, args);
	}

}
