package com.central.core.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
* 密码工具类
*/
@Configuration
public class DefaultPasswordConfig {
	/**
	 * 装配BCryptPasswordEncoder用户密码的匹配
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder()	{
		//return new BCryptPasswordEncoder();
		return new MD5PasswordEncoder();
	}


}
