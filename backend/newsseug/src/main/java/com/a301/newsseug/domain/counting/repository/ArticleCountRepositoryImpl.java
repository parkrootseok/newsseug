package com.a301.newsseug.domain.counting.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ArticleCountRepositoryImpl implements ArticleCountRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String hash, String hashKey, Long value) {
        redisTemplate.opsForHash().put(hash, hashKey, value);
    }

    @Override
    public Map<Object, Object> findByHash(String hash) {
        return redisTemplate.opsForHash().entries(hash);
    }

    @Override
    public void deleteByHash(String hash) {
        redisTemplate.delete(hash);
    }

    @Override
    public Optional<Long> findByKey(String hash, String key) {
        Object value = redisTemplate.opsForHash().get(hash, key);
        if (Objects.nonNull(value)) {
            return Optional.of(Long.valueOf(value.toString()));
        }
        return Optional.empty();
    }

    @Override
    public void deleteByKey(String hash, String key) {
        redisTemplate.opsForHash().delete(hash, key);
    }

    @Override
    public void increment(String hash, String key, Long value) {
        redisTemplate.opsForHash().increment(hash, key, value);
    }

}
