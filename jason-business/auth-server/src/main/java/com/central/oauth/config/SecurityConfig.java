package com.central.oauth.config;

import com.central.core.autoconfigure.DefaultPasswordConfig;
import com.central.core.model.constants.SecurityConstants;
import com.central.oauth.core.mobile.MobileAuthenticationSecurityConfig;
import com.central.oauth.core.openid.OpenIdAuthenticationSecurityConfig;
import com.central.oauth2.common.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.annotation.Resource;


/**
 * spring security配置
 * 在WebSecurityConfigurerAdapter不拦截oauth要开放的资源
 */

@Configuration
//@Order(-1)
@EnableWebSecurity
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

	@Autowired(required = false)
	private AuthenticationEntryPoint authenticationEntryPoint;

	@Resource
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Resource
	private LogoutHandler oauthLogoutHandler;

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	private ValidateCodeSecurityConfig validateCodeSecurityConfig;

	@Autowired
	private OpenIdAuthenticationSecurityConfig openIdAuthenticationSecurityConfig;

	@Autowired
	private MobileAuthenticationSecurityConfig mobileAuthenticationSecurityConfig;

	/**
	 * 这一步的配置是必不可少的，否则SpringBoot会自动配置一个AuthenticationManager,覆盖掉内存中的用户
	 * @return 认证管理对象
	 */
	@Bean
    @Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/*@Bean
	public ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter(AuthenticationManager authenticationManager) throws Exception {
		ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter = new ClientCredentialsTokenEndpointFilter();
		clientCredentialsTokenEndpointFilter.setAuthenticationManager(authenticationManager);
		return clientCredentialsTokenEndpointFilter;
	}*/

	/*@Bean
	public FilterRegistrationBean testFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean(new ClientCredentialsTokenEndpointFilter());
        registration.addUrlPatterns("/oauth/token"); //
        //registration.addInitParameter("paramName", "paramValue"); //
        registration.setName("clientCredentialsTokenEndpointFilter");
		registration.setOrder(1);
		return registration;
	}*/

	/*@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/actuator/**");
	}*/

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
                    .antMatchers( securityProperties.getIgnore().getUrls())
                    .permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage(SecurityConstants.LOGIN_PAGE)
                    .loginProcessingUrl(SecurityConstants.OAUTH_LOGIN_PRO_URL)
                    //.successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .and()
				.logout()
					.logoutUrl(SecurityConstants.LOGOUT_URL)
					.logoutSuccessUrl(SecurityConstants.LOGIN_PAGE)
					.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
					.addLogoutHandler(oauthLogoutHandler)
					.clearAuthentication(true)
					.and()
                .apply(validateCodeSecurityConfig)
                    .and()
                .apply(openIdAuthenticationSecurityConfig)
                    .and()
				.apply(mobileAuthenticationSecurityConfig)
					.and()
                .csrf().disable()
				//.httpBasic().and()
				// 解决不允许显示在iframe的问题
				.headers().frameOptions().disable().cacheControl();

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
		// 基于密码 等模式可以无session,不支持授权码模式
		/*if (authenticationEntryPoint != null) {
			http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		} else {
			// 授权码模式单独处理，需要session的支持，此模式可以支持所有oauth2的认证
			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
		}*/
	}

	/**
	 * 全局用户信息
	 */
	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}
}
