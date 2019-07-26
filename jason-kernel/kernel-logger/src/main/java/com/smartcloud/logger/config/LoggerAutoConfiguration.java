package com.smartcloud.logger.config;


import com.smartcloud.logger.config.properties.LogProperties;
import com.smartcloud.logger.service.LogProducerService;
import com.smartcloud.logger.service.impl.LogProducerServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.central.core.model.constants.ConfigPrefixConstants.LOG_PREFIX;

/**
 * 默认kafka消息队列日志
 */
@Configuration
public class LoggerAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = LOG_PREFIX)
    public LogProperties logProperties() {
        return new LogProperties();
    }

    @Bean
    @ConditionalOnProperty(prefix = LOG_PREFIX, value = "kafka", havingValue = "true")
    public LogProducerService logProducerService() {
        return new LogProducerServiceImpl();
    }
}