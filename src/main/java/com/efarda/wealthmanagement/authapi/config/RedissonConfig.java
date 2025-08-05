package com.efarda.wealthmanagement.authapi.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile(value = {"stage", "prod"})
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password:}")
    private String password;

    @Value("${redis.timeout:3000}")
    private int timeout;

    @Value("${redis.connectionPoolSize:10}")
    private int connectionPoolSize;

    @Value("${redis.connectionMinimumIdleSize:5}")
    private int connectionMinimumIdleSize;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        String redisAddress = "redis://" + host + ":" + port;

        Config config = new Config();
        config.useSingleServer()
                .setAddress(redisAddress)
                .setPassword(password.isBlank() ? null : password)
                .setTimeout(timeout)
                .setConnectionPoolSize(connectionPoolSize)
                .setConnectionMinimumIdleSize(connectionMinimumIdleSize);

        return Redisson.create(config);
    }
}
