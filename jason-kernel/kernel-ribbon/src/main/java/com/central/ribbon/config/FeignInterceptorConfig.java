package com.central.ribbon.config;
import com.central.core.utils.HttpContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * feign拦截器
 */
@Slf4j
public class FeignInterceptorConfig {

    /**
     * 使用feign client访问别的微服务时，将access_token放入参数或者header ，Authorization:Bearer xxx
     * 或者url?access_token=xxx
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        RequestInterceptor requestInterceptor = (RequestTemplate template) -> {
            /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                if (authentication instanceof OAuth2Authentication) {
                    OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
                    String access_token = details.getTokenValue();
                    template.header("Authorization", OAuth2AccessToken.BEARER_TYPE + " " + access_token);
                }
            }*/
            HttpServletRequest request = HttpContext.getRequest();
            if (request == null) {
                if (log.isDebugEnabled()) {
                    log.debug("被调环境中不存在request对象，则不往header里添加当前请求环境的header!");
                }
                return;
            } else {
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        String values = request.getHeader(name);
                        template.header(name, values);
                    }
                }
            }
           /* RequestContext currentContext = RequestContext.getCurrentContext();
            Object contextObject = currentContext.get(JasonConstants.REQUEST_NO_HEADER_NAME);
            template.header(JasonConstants.REQUEST_NO_HEADER_NAME, contextObject == null ? "" : contextObject.toString());*/
        };
        return requestInterceptor;
    }
}
