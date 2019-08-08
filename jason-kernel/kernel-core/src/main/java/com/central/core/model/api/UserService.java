package com.central.core.model.api;

import com.central.core.model.user.LoginAppUser;
import com.central.core.model.user.SysUser;
import org.springframework.web.bind.annotation.*;

//@RequestMapping("/api/users")
public interface UserService {
    /**
     * feign rpc访问远程/users/{username}接口
     * 查询用户实体对象SysUser
     *
     * @param username
     * @return
     */
    @GetMapping(value = "/users/name/{username}")
    SysUser selectByUsername(@PathVariable("username") String username);

    /**
     * feign rpc访问远程/users-anon/login接口
     *
     * @param username
     * @return
     */
    //@GetMapping(value = "/users-anon/login", params = "username")
    //@RequestMapping(method = RequestMethod.GET, value = "/users-anon/login", params = "username")
    @RequestMapping(method = { RequestMethod.GET }, produces="application/json;charset=UTF-8",params = "username")
    LoginAppUser findByUsername(@RequestParam("username") String username);

    /**
     * 通过手机号查询用户、角色信息
     *
     * @param mobile 手机号
     */
    @GetMapping(value = "/users-anon/mobile", params = "mobile")
    LoginAppUser findByMobile(@RequestParam("mobile") String mobile);

    /**
     * 根据OpenId查询用户信息
     *
     * @param openId openId
     */
    @GetMapping(value = "/users-anon/openId", params = "openId")
    LoginAppUser findByOpenId(@RequestParam("openId") String openId);
}
