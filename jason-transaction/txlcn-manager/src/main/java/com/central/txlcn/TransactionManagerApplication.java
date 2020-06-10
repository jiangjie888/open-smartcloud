package com.central.txlcn;

import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @program: open-smartcloud
 * @description:
 * @author: jason
 * @create: 2020-06-02 15:36
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableTransactionManagerServer
public class TransactionManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransactionManagerApplication.class, args);
    }
}
