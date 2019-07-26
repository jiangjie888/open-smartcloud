package com.central.oauth.modular.service.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

import java.util.concurrent.TimeUnit;

/**
 * JdbcAuthorizationCodeServices替换 授权码模式
 */
public class RedisAuthorizationCodeServices extends RandomValueAuthorizationCodeServices {

    private RedisTemplate<String, Object> redisTemplate;

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 存储code到redis，并设置过期时间，10分钟<br>
     * value为OAuth2Authentication序列化后的字节<br>
     * 因为OAuth2Authentication没有无参构造函数<br>
     * redisTemplate.opsForValue().set(key, value, timeout, unit);
     * 这种方式直接存储的话，redisTemplate.opsForValue().get(key)的时候有些问题，
     * 所以这里采用最底层的方式存储，get的时候也用最底层的方式获取
     */
    @Override
    protected void store(String code, OAuth2Authentication authentication) {
        redisTemplate.opsForValue().set(redisKey(code), authentication, 10, TimeUnit.MINUTES);

        /*redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(codeKey(code).getBytes(), SerializationUtils.serialize(authentication),
                        Expiration.from(10, TimeUnit.MINUTES), SetOption.UPSERT);
                return 1L;
            }
        });*/
    }

    @Override
    protected OAuth2Authentication remove(final String code) {
        String codeKey = redisKey(code);
        OAuth2Authentication token = (OAuth2Authentication) redisTemplate.opsForValue().get(codeKey);
        this.redisTemplate.delete(codeKey);
        return token;

        /*OAuth2Authentication token = redisTemplate.execute(new RedisCallback<OAuth2Authentication>() {

            @Override
            public OAuth2Authentication doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyByte = codeKey(code).getBytes();
                byte[] valueByte = connection.get(keyByte);

                if (valueByte != null) {
                    connection.del(keyByte);
                    return SerializationUtils.deserialize(valueByte);
                }

                return null;
            }
        });
        return token;*/
    }

    /**
     * redis中 code key的前缀
     *
     * @param code
     * @return
     */
    private String redisKey(String code) {
        return "oauth:code:" + code;
    }
}
