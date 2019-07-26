package com.central.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class NacosDiscoveryServerClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(NacosDiscoveryServerClientApplication.class, args);
	}

}
