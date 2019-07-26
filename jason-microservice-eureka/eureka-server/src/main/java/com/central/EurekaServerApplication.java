package com.central;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@EnableEurekaServer
//@EnableEurekaClient
@SpringBootApplication
public class EurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);

    	/*//1 本地启动采用此方法加载profiles文件
		ConfigurableApplicationContext context = new SpringApplicationBuilder(UnieapEurekaServerApplication.class).
				profiles("slave3").run(args);

   		//2 服务器采用此方法 java -jar   --spring.profiles.active=slave3;
   	 	SpringApplication.run(DreiEurekaServerApp.class, args);
		ConfigurableApplicationContext context = new SpringApplicationBuilder(EurekaServerApplication.class).
				profiles("slave0").run(args);*/

	}
}
