package com.central.core.autoconfigure;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

import static cn.hutool.core.util.StrUtil.bytes;

/**
 * @program: open-smartcloud
 * @description:
 * @author: jason
 * @create: 2019-07-12 17:19
 **/
public class MD5PasswordEncoder  implements PasswordEncoder {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String encode(CharSequence rawPassword) {
        logger.debug("加密时待加密的密码：" + rawPassword.toString());
        return (new MD5()).digestHex(rawPassword.toString(),"UTF-8");
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        //string test = SecureUtil.hmacMd5(rawPassword);
        //return new HMac(HmacAlgorithm.HmacMD5, key);
        //byte[] tempData = StrUtil.bytes(rawPassword,"UTF-8");
        String tempPassword = (new MD5()).digestHex(rawPassword.toString(),"UTF-8");
        //SecureUtil.hmacMd5().digestHex("webApp","UTF-8");

        logger.debug("校验时待加密的密码：" + rawPassword.toString());
        logger.debug("校验时已加密的密码：" + encodedPassword);
        return  encodedPassword.equals(tempPassword);
    }
}
