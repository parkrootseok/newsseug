package com.a301.newsseug.domain.counting.service;

import com.a301.newsseug.domain.counting.event.CountingEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountingServiceImpl implements CountingService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(String hash, Long hashKey, Long value) {
        redisTemplate.opsForHash().put(hash, hashKey.toString(), value);
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
    public Optional<Long> findByKey(String hash, Long hashKey) {

        Number value = (Number) redisTemplate.opsForHash().get(hash, hashKey.toString());

        if (Objects.nonNull(value)) {
            return Optional.of(value.longValue());
        }

        return Optional.empty();

    }

    @Override
    public void deleteByKey(String hash, Long hashKey) {
        redisTemplate.opsForHash().delete(hash, hashKey.toString());
    }

    @Override
    public Long increment(String hash, Long hashKey, Long value) {
        return redisTemplate.opsForHash().increment(hash, hashKey.toString(), value);
    }

}
