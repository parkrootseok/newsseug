package com.a301.newsseug.domain.counting.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CountRedisHashRepositoryImpl implements CountRedisHashRepository {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String hash, String hashKey, Long value) {
        redisTemplate.opsForHash().put(hash, hashKey, value);
    }

    @Override
    public Map<String, Long> getAndDelByHash(String hash) {
        String script = """
            local result = redis.call('HGETALL', KEYS[1])
            if #result > 0 then
                local keys = {}
                for i = 1, #result, 2 do
                    table.insert(keys, result[i])
                end
                redis.call('HDEL', KEYS[1], unpack(keys))
            end
            return result
        """;

        List<Object> results = redisTemplate.execute(
                new DefaultRedisScript<>(script, List.class),
                Collections.singletonList(hash)
        );

        Map<String, Long> map = new HashMap<>();
        if (Objects.nonNull(results)) {
            for (int offset = 0; offset < results.size(); offset += 2) {
                String field = (String) results.get(offset);
                String valueStr = (String) results.get(offset + 1);
                try {
                    Long value = Long.parseLong(valueStr);
                    map.put(field, value);
                } catch (NumberFormatException e) {
                    log.warn("Failed to parse value for field: {} with value: {}", field, valueStr);
                }
            }
        }

        return map;
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
