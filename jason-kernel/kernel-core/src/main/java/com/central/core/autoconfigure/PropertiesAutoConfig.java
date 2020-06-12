package com.central.core.autoconfigure;

import com.central.core.autoconfigure.properties.AppNameProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 默认的配置
 */
@Configuration
//@PropertySource("classpath:/application.yml")
public class PropertiesAutoConfig {

    @Lazy
    @Bean
    @ConfigurationProperties(prefix = "spring.application")
    public AppNameProperties appNameProperties() {
        return new AppNameProperties();
    }
}
