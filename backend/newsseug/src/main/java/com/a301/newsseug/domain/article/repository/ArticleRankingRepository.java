package com.a301.newsseug.domain.article.repository;

import com.a301.newsseug.external.redis.repository.RedisZSetRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ArticleRankingRepository implements RedisZSetRepository<String, String> {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void save(String key, String value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public void incrementScore(String key, String value, double score) {
        redisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    @Override
    public Set<TypedTuple<String>> getTopN(String key, long n) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, n - 1);
    }

    @Override
    public void deleteByKey(String key) {
        redisTemplate.delete(key);
    }

}
