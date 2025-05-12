package com.a301.newsseug.external.redis.repository;

import java.util.Set;
import org.springframework.data.redis.core.ZSetOperations;

public interface RedisZSetRepository<Z, V> {

    void save(Z key, V value, double score);

    void incrementScore(Z key, V value, double score);

    Set<ZSetOperations.TypedTuple<V>> getTopN(Z key, long n);

    void deleteByKey(Z key);

}
