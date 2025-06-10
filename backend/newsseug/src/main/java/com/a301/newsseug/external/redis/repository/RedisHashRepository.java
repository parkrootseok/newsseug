package com.a301.newsseug.external.redis.repository;

import java.util.Map;
import java.util.Optional;

public interface RedisHashRepository<H, K, V> {

    void save(H hash, K key, V value);
    Map<K, V> getAndDelByHash(H hash);
    void deleteByHash(H hash);
    Optional<V> findByKey(H hash, K key);
    void deleteByKey(H hash, K key);
    void increment(H hash, K key, V value);

}
