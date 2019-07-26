package com.central;

import com.central.core.annotation.EnableLogging;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 消息服务子系统
 */

@EnableDiscoveryClient
@EnableFeignClients
@EnableLogging
@MapperScan(basePackages = {"com.central.message.dao*","com.central.core.log.dao*"})
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class JasonMessageServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(JasonMessageServiceApplication.class, args);
	}
}
