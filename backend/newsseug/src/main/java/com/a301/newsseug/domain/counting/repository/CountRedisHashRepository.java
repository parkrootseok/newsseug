package com.a301.newsseug.domain.counting.repository;

import com.a301.newsseug.external.redis.repository.RedisHashRepository;

public interface CountRedisHashRepository extends RedisHashRepository<String, String, Long> {

}
