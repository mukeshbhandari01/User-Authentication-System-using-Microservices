package com.roadways.login.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {
    private final StringRedisTemplate redis;

    public RedisService(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public void blacklist(String token, Duration ttl) {
        redis.opsForValue().set("blacklist:jwt:" + token, "1", ttl);
    }

    public boolean blacklisted(String token) {
        return Boolean.TRUE.equals(redis.hasKey("blacklist:jwt:" + token));
    }
}
