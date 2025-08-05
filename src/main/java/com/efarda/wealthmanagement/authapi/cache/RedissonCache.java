package com.efarda.wealthmanagement.authapi.cache;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Profile({"stage", "prod"})
public class RedissonCache implements CacheService {

    private final RedissonClient redissonClient;

    public RedissonCache(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public String get(String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    @Override
    public void put(String key, String value, Duration ttl) {
        final var bucket = redissonClient.getBucket(key);
        bucket.set(value, ttl);
    }

    @Override
    public void invalidate(String key) {
        final var bucket = redissonClient.getBucket(key);
        bucket.delete();
    }
}
