package com.central.ribbon;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * Feign统一配置
 */
public class FeignAutoConfigure {

    /**
     * Feign 日志级别
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
