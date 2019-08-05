package com.central.oauth.modular.service.impl;

import org.springframework.security.oauth2.common.util.RandomValueStringGenerator;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;

import javax.sql.DataSource;

/**
 *
 **/
public class CustomJdbcAuthorizationCodeServices  extends JdbcAuthorizationCodeServices {

    private RandomValueStringGenerator generator = new RandomValueStringGenerator();

    public CustomJdbcAuthorizationCodeServices(DataSource dataSource) {
        super(dataSource);
        this.generator = new RandomValueStringGenerator(32);
    }

    public String createAuthorizationCode(OAuth2Authentication authentication) {
        String code = this.generator.generate();
        store(code, authentication);
        return code;
    }

}
