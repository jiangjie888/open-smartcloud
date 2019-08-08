package com.central.infra.modular.provider;

import com.central.core.model.api.UserService;
import com.central.core.model.user.LoginAppUser;
import com.central.core.model.user.SysUser;
import com.central.infra.modular.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: open-smartcloud
 * @description:
 * @author: jason
 * @create: 2019-07-02 13:28
 **/

@Api(tags = "用户查询相关服务")
@RestController
@Primary
public class UserServiceProvider implements UserService {

    @Autowired
    private ISysUserService sysUserService;

    /**
     * feign rpc访问远程/users/{username}接口
     * 查询用户实体对象SysUser
     * @param username
     * @return
     */
    @Override
    //@GetMapping(value = "/users/name/{username}")
    @ApiOperation(value = "根据用户名查询用户实体")
    //@Cacheable(value = "user", key = "#username")
    public SysUser selectByUsername(@PathVariable("username") String username)
    {
        return sysUserService.selectByUsername(username);
    }

    /**
     * feign rpc访问远程/users-anon/login接口
     * @param username
     * @return
     */
    @Override
    //@GetMapping(value = "/users-anon/login", params = "username")
    @ApiOperation(value = "根据用户名查询用户")
    public LoginAppUser findByUsername(@RequestParam("username") String username)
    {
        LoginAppUser u = sysUserService.findByUsername(username);
        return u;
    }

    /**
     * 通过手机号查询用户、角色信息
     * @param mobile 手机号
     */
    @Override
    //@GetMapping(value = "/users-anon/mobile", params = "mobile")
    @ApiOperation(value = "根据手机号查询用户")
    public LoginAppUser findByMobile(@RequestParam("mobile") String mobile)
    {
        return sysUserService.findByMobile(mobile);
    }

    /**
     * 根据OpenId查询用户信息
     * @param openId openId
     */
    @Override
    //@GetMapping(value = "/users-anon/openId", params = "openId")
    @ApiOperation(value = "根据OpenId查询用户")
    public LoginAppUser findByOpenId(@RequestParam("openId") String openId)
    {
        return sysUserService.findByOpenId(openId);
    }
}