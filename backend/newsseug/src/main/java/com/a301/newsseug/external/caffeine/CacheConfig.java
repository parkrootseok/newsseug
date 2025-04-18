package com.a301.newsseug.external.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        List<CaffeineCache> caches =
                Arrays.stream(CacheTypes.values())
                        .map(c -> new CaffeineCache(
                                c.getName(),
                                Caffeine.newBuilder()
                                        .maximumSize(c.getMaximumSize())
                                        .expireAfterWrite(c.getExpireAfterWrite(), TimeUnit.SECONDS)
                                        .recordStats()
                                        .build()
                                )).toList();


        cacheManager.setCaches(caches);
        return cacheManager;
    }

}
