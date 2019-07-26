package com.central.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class NacosDiscoveryServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NacosDiscoveryServerApplication.class, args);
	}

}
