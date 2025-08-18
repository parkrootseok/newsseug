package com.a301.newsseug.domain.press.service;

import com.a301.newsseug.domain.press.model.entity.Press;
import com.a301.newsseug.domain.press.repository.PressRepository;
import com.a301.newsseug.external.caffeine.CacheTypes;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PressCacheManager {

    private final CacheManager cacheManager;
    private final PressRepository pressRepository;

    @Cacheable(value = "press", key = "#id")
    public Press getCachedPress(Long id) {
        return pressRepository.findOrThrow(id);
    }

    public Map<Long, Press> getPressMapFromCache(Set<Long> ids) {
        return ids.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(id -> id, this::getCachedPress));
    }

    public void evict(Long id) {
        Objects.requireNonNull(cacheManager.getCache(CacheTypes.PRESS.getName())).evict(id);
    }

    public void evictBatch(Set<Long> ids) {
        var cache = Objects.requireNonNull(cacheManager.getCache(CacheTypes.PRESS.getName()));
        ids.forEach(cache::evict);
    }

}
