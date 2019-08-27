package com.central;

import com.central.search.core.properties.IndexProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableConfigurationProperties(IndexProperties.class)
public class SearchServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(SearchServerApplication.class, args);
	}
}