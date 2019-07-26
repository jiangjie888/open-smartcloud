package com.central.client.oauth2.authorize.provider;

import com.central.oauth2.common.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import com.central.client.oauth2.authorize.AuthorizeConfigProvider;

/**
 * 白名单
 */
@Component
@Order(Integer.MAX_VALUE - 1)
//@EnableConfigurationProperties(PermitUrlProperties.class)
public class AuthAuthorizeConfigProvider implements AuthorizeConfigProvider {

	@Autowired
	private SecurityProperties securityProperties;

	/*@Autowired(required = false)
	private PermitUrlProperties permitUrlProperties;*/

	@Override
	public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {

		// 免token登录设置
		config.antMatchers(securityProperties.getIgnore().getUrls()).permitAll();

		config.antMatchers(HttpMethod.OPTIONS).permitAll();
		return true;
	}

}
