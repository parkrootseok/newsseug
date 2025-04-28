package com.a301.newsseug.external.redisson.config;

import com.a301.newsseug.external.redis.config.RedisProperties;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RedissonConfig {

    private final RedisProperties properties;

    @Bean
    RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + properties.host() + ":" + properties.port());
        return Redisson.create(config);
    }

}
