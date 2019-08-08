package com.central.oauth.config;


import com.central.core.model.reqres.response.ErrorResponseData;
import com.central.core.utils.ResponseUtil;
import com.central.oauth.core.handler.OauthLogoutHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证错误处理
 */
@Slf4j
@Configuration
public class SecurityHandlerConfig {
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 登陆失败，返回401
     */
    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        return (request, response, exception) -> {
            String msg;
            if (exception instanceof BadCredentialsException) {
                msg = "密码错误";
            } else {
                msg = exception.getMessage();
            }
            ResponseUtil.responseWriter(objectMapper, response, msg, HttpStatus.UNAUTHORIZED.value());
            /*log.info("登录失败");
            if (LoginTypeEnum.JSON.equals(securityProperties.getBrowser().getLoginType())) {
                response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JSON.toJSONString(exception));
            } else {
                // 如果用户配置为跳转，则跳到Spring Boot默认的错误页面
                super.onAuthenticationFailure(request, response, exception);
            }*/
        };
    }
    /**
     * 登陆成功，返回Token 装配此bean不支持授权码模式
     */
    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return new SavedRequestAwareAuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                super.onAuthenticationSuccess(request, response, authentication);
                /*log.info("登录成功");
                if(LoginTypeEnum.JSON.equals(securityProperties.getBrowser().getLoginType())){
                    //返回json处理 默认也是json处理
                    response.setContentType("application/json;charset=UTF-8");
                    log.info("认证信息:"+JSON.toJSONString(authentication));
                    response.getWriter().write(JSON.toJSONString(authentication));
                } else {
                    // 如果用户定义的是跳转，那么就使用父类方法进行跳转
                    super.onAuthenticationSuccess(request, response, authentication);
                }*/
            }
        };
    }

    /**
     * 销毁token
     */
    @Bean
    public OauthLogoutHandler oauthLogoutHandler() {
        return new OauthLogoutHandler();
    }

    @Bean
    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
        return new DefaultWebResponseExceptionTranslator() {
            public static final String BAD_MSG = "Bad credentials"/*"坏的凭证"*/;

            @Override
            public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
                OAuth2Exception oAuth2Exception;
                if (e.getMessage() != null && e.getMessage().equals(BAD_MSG)) {
                    oAuth2Exception = new InvalidGrantException("用户名或密码错误", e);
                } else if (e instanceof InternalAuthenticationServiceException) {
                    oAuth2Exception = new InvalidGrantException(e.getMessage(), e);
                } else if (e instanceof RedirectMismatchException) {
                    oAuth2Exception = new InvalidGrantException(e.getMessage(), e);
                } else if (e instanceof InvalidScopeException) {
                    oAuth2Exception = new InvalidGrantException(e.getMessage(), e);
                } else {
                    oAuth2Exception = new UnsupportedResponseTypeException("服务内部错误", e);
                }
                ResponseEntity<OAuth2Exception> response = super.translate(oAuth2Exception);
                ResponseEntity.status(oAuth2Exception.getHttpErrorCode());

                //ResponseUtil.responseWriter(objectMapper, response, oAuth2Exception.getMessage(), oAuth2Exception.getHttpErrorCode());
                log.info(e.getClass().getName()+"------"+oAuth2Exception.getHttpErrorCode()+"："+oAuth2Exception.getMessage());
                response.getBody().addAdditionalInformation("success", false + "");
                response.getBody().addAdditionalInformation("code", oAuth2Exception.getHttpErrorCode() + "");
                response.getBody().addAdditionalInformation("message", oAuth2Exception.getMessage());

                return response;
            }
        };
    }


}
