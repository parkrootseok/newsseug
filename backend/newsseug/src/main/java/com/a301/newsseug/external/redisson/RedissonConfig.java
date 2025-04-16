package com.a301.newsseug.external.redisson;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RedissonConfig {

    private final Environment env;

    @Bean
    RedissonClient redissonClient() {

        String host = Objects.requireNonNull(env.getProperty("spring.data.redis.host"), "Redis Host is null");
        int port = Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.data.redis.port"), "Redis Port is null"));

        log.info(">>> Redis Host: {}, Port: {}", host, port);

        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port);

        return Redisson.create(config);
    }

}