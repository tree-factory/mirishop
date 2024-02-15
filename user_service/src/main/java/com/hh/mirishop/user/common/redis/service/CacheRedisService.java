package com.hh.mirishop.user.common.redis.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CacheRedisService {

    private final RedisTemplate<String, String> cacheRedisTemplate;

    public CacheRedisService(@Qualifier("cacheRedisTemplate") RedisTemplate<String, String> cacheRedisTemplate) {
        this.cacheRedisTemplate = cacheRedisTemplate;
    }

    public String getData(String key) {
        ValueOperations<String, String> valueOperations = cacheRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setData(String key, String value) {
        ValueOperations<String, String> valueOperations = cacheRedisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = cacheRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteData(String key) {
        cacheRedisTemplate.delete(key);
    }
}