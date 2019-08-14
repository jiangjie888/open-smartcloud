package com.central.user.config;

import com.central.logger.chain.aop.ChainOnConsumerAop;
import com.central.logger.chain.aop.ChainOnControllerAop;
import com.central.logger.chain.aop.ChainOnProviderAop;
import com.central.logger.config.DefaultLogConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志服务的自动配置
 */
@Configuration
public class LogConfig extends DefaultLogConfig {
/*
    *//**
     * 日志拦截器 controller
     *//*
    @Bean
    public ChainOnControllerAop chainOnControllerAop() {
        return new ChainOnControllerAop();
    }

    *//**
     * 日志拦截器 provider
     *//*
    @Bean
    public ChainOnProviderAop chainOnProviderAop() {
        return new ChainOnProviderAop();
    }

    *//**
     * 日志拦截器 consumer
     *//*
    @Bean
    public ChainOnConsumerAop chainOnConsumerAop() {
        return new ChainOnConsumerAop();
    }*/
}
