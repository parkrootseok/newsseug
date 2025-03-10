package com.a301.newsseug.domain.counting.service;

import com.a301.newsseug.external.redis.service.RedisHashService;

public interface CountingService extends RedisHashService<String, Long, Long> {

}
