package com.central.core.autoconfigure;

import com.central.core.autoconfigure.aop.ParamValidateAop;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 签名的自动配置
 */
@Configuration
public class ValidatorAutoConfig {

    @Bean
    public ParamValidateAop paramValidateAop() {
        return new ParamValidateAop();
    }
}
