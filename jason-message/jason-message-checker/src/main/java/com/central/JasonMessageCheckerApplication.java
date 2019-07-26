package com.central;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.central.message.checker.consumer")
@EnableDiscoveryClient
@EnableScheduling
public class JasonMessageCheckerApplication {

	public static void main(String[] args) {
		SpringApplication.run(JasonMessageCheckerApplication.class, args);
	}
}
