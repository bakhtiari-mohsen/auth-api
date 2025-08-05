package com.efarda.wealthmanagement.authapi.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Profile("dev")
public class CaffeineCache implements CacheService {

    private final LoadingCache<String, String> cache;

    public CaffeineCache() {
        this.cache = Caffeine.newBuilder()
                //.expireAfterWrite(Duration.ofMinutes(5))
                //.maximumSize(1)
                .build(key -> null); // no auto load, manual put
    }

    @Override
    public String get(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void put(String key, String value, Duration ttl) {
        cache.put(key, value);
    }

    @Override
    public void invalidate(String key) {
        cache.invalidate(key);
    }
}
