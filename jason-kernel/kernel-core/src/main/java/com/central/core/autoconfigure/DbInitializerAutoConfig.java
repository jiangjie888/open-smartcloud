package com.central.core.autoconfigure;

import com.central.core.db.listener.InitTableListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据库初始化默认配置
 */
@Configuration
public class DbInitializerAutoConfig {

    @Bean
    public InitTableListener initTableListener() {
        return new InitTableListener();
    }
}


