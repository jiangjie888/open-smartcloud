package com.central.demo.controller;

import com.central.core.model.user.LoginAppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @program: open-smartcloud
 * @description:
 * @author: jason
 * @create: 2019-05-23 15:25
 **/
@Slf4j
@RestController
@RefreshScope   //主要用来让这个类下的配置内容支持动态刷新
public class TestController {

    @Resource
    private UserServiceConsumer userServiceConsumer;

    @Value("${didispace.title:}")
    private String title;

    @GetMapping("/test")
    public String hello() {
        return title;
    }

    @GetMapping("/testFeign")
    public LoginAppUser testFeign() {
        LoginAppUser loginAppUser = userServiceConsumer.findByUsername("admin");
        if (loginAppUser == null) {
            throw new InternalAuthenticationServiceException("Feign获取用户失败");
        }
        return loginAppUser;
    }

}