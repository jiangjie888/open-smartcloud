package com.central.oauth.modular.consumer.fallback;

import com.central.core.model.user.LoginAppUser;
import com.central.core.model.user.SysUser;
import com.central.oauth.modular.consumer.UserServiceConsumer;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * userService降级工场
 */
@Slf4j
@Component
public class UserServiceFallbackFactory implements FallbackFactory<UserServiceConsumer> {
    @Override
    public UserServiceConsumer create(Throwable throwable) {
        return new UserServiceConsumer() {
            @Override
            public SysUser selectByUsername(String username) {
                log.error("通过用户名查询用户异常:{}", username, throwable);
                return new SysUser();
            }

            @Override
            public LoginAppUser findByUsername(String username) {
                log.error("通过用户名查询用户异常:{}", username, throwable);
                return new LoginAppUser();
            }

            @Override
            public LoginAppUser findByMobile(String mobile) {
                log.error("通过手机号查询用户异常:{}", mobile, throwable);
                return new LoginAppUser();
            }

            @Override
            public LoginAppUser findByOpenId(String openId) {
                log.error("通过openId查询用户异常:{}", openId, throwable);
                return new LoginAppUser();
            }
        };
    }
}
