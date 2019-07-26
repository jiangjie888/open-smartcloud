package com.central;

import com.central.core.annotation.EnableLogging;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients("com.central.demo.order.consumer")
@EnableLogging
@MapperScan(basePackages = {"com.central.demo.order.dao*","com.central.core.log.dao*"})
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class JasonDemoOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(JasonDemoOrderApplication.class, args);
	}
}
