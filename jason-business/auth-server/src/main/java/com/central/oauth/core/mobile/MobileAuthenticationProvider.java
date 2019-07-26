package com.central.oauth.core.mobile;

import com.central.oauth.modular.service.BaseUserDetailsService;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 */
@Setter
public class MobileAuthenticationProvider implements AuthenticationProvider {
    private BaseUserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) {
        com.central.oauth.core.mobile.MobileAuthenticationToken authenticationToken = (com.central.oauth.core.mobile.MobileAuthenticationToken) authentication;
        String mobile = (String) authenticationToken.getPrincipal();
        String password = (String) authenticationToken.getCredentials();
        UserDetails user = userDetailsService.loadUserByMobile(mobile);
        if (user == null) {
            throw new InternalAuthenticationServiceException("手机号错误");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }
        com.central.oauth.core.mobile.MobileAuthenticationToken authenticationResult = new com.central.oauth.core.mobile.MobileAuthenticationToken(user, password, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
