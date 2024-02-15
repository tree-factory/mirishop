package com.hh.mirishop.user.common.redis.service;

import com.hh.mirishop.user.common.exception.ErrorCode;
import com.hh.mirishop.user.common.exception.JwtTokenException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class AuthRedisService {

    private final RedisTemplate<String, String> authRedisTemplate;

    public AuthRedisService(@Qualifier("redisTemplate") RedisTemplate<String, String> authRedisTemplate) {
        this.authRedisTemplate = authRedisTemplate;
    }

    public String getData(String key) {
        ValueOperations<String, String> valueOperations = authRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setData(String key, String value) {
        ValueOperations<String, String> valueOperations = authRedisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = authRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteData(String key) {
        authRedisTemplate.delete(key);
    }

    public void checkRefreshToken(String email, String refreshToken) {
        String redisRT = this.getData(email);
        if (!refreshToken.equals(redisRT)) {
            throw new JwtTokenException(ErrorCode.EXPIRED_TOKEN);
        }
    }
}