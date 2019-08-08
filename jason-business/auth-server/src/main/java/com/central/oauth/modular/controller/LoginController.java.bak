package com.central.oauth.modular.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.central.core.model.reqres.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
public class LoginController {

    //封装了引发跳转请求的工具类  https://blog.csdn.net/honghailiang888/article/details/53671108
    private RequestCache requestCache = new HttpSessionRequestCache();
    // spring的工具类：封装了所有跳转行为策略类
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private SecurityProperties securityProperties;

    private static final String HTML_SUFFIX = ".html";


    /**
     * 当需要进行身份认证的时候跳转到此方法
     *
     * @param request  请求
     * @param response 响应
     * @return 将信息以JSON形式返回给前端
     */
    @RequestMapping("/loginn")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseData requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 从session缓存中获取引发跳转的请求
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (null != savedRequest) {
            String redirectUrl = savedRequest.getRedirectUrl();
            log.info("引发跳转的请求是：{}", redirectUrl);
            if (StringUtils.endsWithIgnoreCase(redirectUrl, HTML_SUFFIX)) {
                // 如果是HTML请求，那么就直接跳转到HTML，不再执行后面的代码
                //redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
            }
        }
        return new ResponseData().error("访问的服务需要身份认证，请引导用户到登录页面");
    }
}
