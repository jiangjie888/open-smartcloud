package com.central.oauth.config;

import com.central.oauth.modular.service.impl.CustomJdbcAuthorizationCodeServices;
import com.central.oauth.modular.service.impl.RedisClientDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;

import javax.annotation.Resource;
import javax.sql.DataSource;


@Configuration
public class ClientDetailsConfig {
    @Resource
    private DataSource dataSource;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 声明 ClientDetails实现
     */
    @Bean
    public RedisClientDetailsService clientDetailsService() {
        RedisClientDetailsService clientDetailsService = new RedisClientDetailsService(dataSource);
        clientDetailsService.setRedisTemplate(redisTemplate);
        return clientDetailsService;
    }

    /**
     * 授权码code
     */
    /*@Bean
    public RandomValueAuthorizationCodeServices authorizationCodeServices() {
        RedisAuthorizationCodeServices redisAuthorizationCodeServices = new RedisAuthorizationCodeServices();
        redisAuthorizationCodeServices.setRedisTemplate(redisTemplate);
        return redisAuthorizationCodeServices;
    }*/

    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        return new CustomJdbcAuthorizationCodeServices(dataSource);
    }
}
