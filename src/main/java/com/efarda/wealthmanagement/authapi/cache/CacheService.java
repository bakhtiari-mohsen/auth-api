package com.efarda.wealthmanagement.authapi.cache;

import java.time.Duration;

public interface CacheService {
    String get(String key);
    void put(String key, String value, Duration ttl);
    void invalidate(String key);
}
