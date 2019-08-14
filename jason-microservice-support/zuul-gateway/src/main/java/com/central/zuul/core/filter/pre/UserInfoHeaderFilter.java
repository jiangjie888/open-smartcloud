package com.central.zuul.core.filter.pre;

import cn.hutool.core.collection.CollectionUtil;
import com.central.core.model.constants.CommonConstant;
import com.central.core.model.constants.SecurityConstants;
import com.central.core.model.user.LoginAppUser;
import com.central.zuul.core.constants.ZuulFiltersOrder;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER;

/**
 * 将认证用户的相关信息放入header中, 后端服务可以直接读取使用
 */
@Component
public class UserInfoHeaderFilter extends ZuulFilter {
    @Override
    public String filterType() {
        //前置过滤器
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        //优先级，数字越大，优先级越低
        return ZuulFiltersOrder.USER_TOKEN_FILTER_ORDER;
        //return FORM_BODY_WRAPPER_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        //是否执行该过滤器，true代表需要过滤
        return true;
    }

    @Override
    public Object run() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();
            String username;
            Long userid=0L;
            if (principal instanceof LoginAppUser) {
                LoginAppUser user = (LoginAppUser) principal;
                username = user.getUsername();
                userid = user.getId();
            } else {
                //jwt的token只有name
                username = authentication.getName();
            }
            OAuth2Authentication oauth2Authentication = (OAuth2Authentication)authentication;
            String clientId = oauth2Authentication.getOAuth2Request().getClientId();

            RequestContext ctx = RequestContext.getCurrentContext();
            ctx.addZuulRequestHeader(SecurityConstants.USER_HEADER, username);
            ctx.addZuulRequestHeader(SecurityConstants.ROLE_HEADER, CollectionUtil.join(authentication.getAuthorities(), ","));
            ctx.addZuulRequestHeader(SecurityConstants.USER_ID_HEADER, String.valueOf(userid));
            ctx.addZuulRequestHeader(SecurityConstants.TENANT_HEADER, clientId);
            //OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) athentication.getDetails() ;
            //ctx.addZuulRequestHeader("Authorization", OAuth2AccessToken.BEARER_TYPE+ " " + details.getTokenValue());
        }
        return null;
    }
}
