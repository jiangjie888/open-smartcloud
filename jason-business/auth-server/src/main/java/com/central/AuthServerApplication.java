package com.central;
import com.central.ribbon.annotation.EnableFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@EnableFeignClients
@EnableDiscoveryClient
@EnableRedisHttpSession
@EnableFeignInterceptor
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class AuthServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthServerApplication.class, args);
	}
}
