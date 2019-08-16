package com.central.oauth.modular.consumer;

/**
 * @program: open-smartcloud
 * @description:
 * @author: jason
 * @create: 2019-07-02 11:07
 **/

import com.central.core.model.api.UserService;
import com.central.core.model.constants.SecurityConstants;
import com.central.core.model.user.LoginAppUser;
import com.central.core.model.user.SysUser;
import com.central.oauth.modular.consumer.fallback.UserServiceFallbackFactory;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户服务消费者")
@FeignClient(name = "user-server", configuration =ServiceFeignConfiguration.class, fallbackFactory = UserServiceFallbackFactory.class, decode404 = true)
//@FeignClient(name = "user-server")
public interface UserServiceConsumer extends UserService {
    /**
     * feign rpc访问远程/users/{username}接口
     * 查询用户实体对象SysUser
     *
     * @param username
     * @return
     */
    //@GetMapping(value = "/users/name/{username}")
    //SysUser selectByUsername(@PathVariable("username") String username);

    /**
     * feign rpc访问远程/users-anon/login接口
     *
     * @param username
     * @return
     */
    //@RequestMapping(method = { RequestMethod.GET }, value = "/users-anon/login", params = "username", produces="application/json;charset=UTF-8")
    //LoginAppUser findByUsername(@RequestParam("username") String username);

    /*
    **
     * 通过手机号查询用户、角色信息
     *
     * @param mobile 手机号
     */
    //@GetMapping(value = "/users-anon/mobile", params = "mobile")
    //LoginAppUser findByMobile(@RequestParam("mobile") String mobile);

    /**
     * 根据OpenId查询用户信息
     *
     * @param openId openId
     */
    //@GetMapping(value = "/users-anon/openId", params = "openId")
    //LoginAppUser findByOpenId(@RequestParam("openId") String openId);
}