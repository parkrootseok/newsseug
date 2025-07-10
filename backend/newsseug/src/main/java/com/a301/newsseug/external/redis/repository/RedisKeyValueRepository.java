package com.a301.newsseug.external.redis.repository;

import java.time.Duration;
import java.util.Optional;

public interface RedisKeyValueRepository<K, T> {

    void saveWithTTL(K key, T value, Duration duration);
    Optional<T> findByKey(K key);
    Boolean deleteByKey(K key);

}
