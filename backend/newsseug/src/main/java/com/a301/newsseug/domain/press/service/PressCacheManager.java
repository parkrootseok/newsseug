package com.a301.newsseug.domain.press.service;

import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.press.model.entity.Press;
import com.a301.newsseug.domain.press.repository.PressRepository;
import com.a301.newsseug.external.caffeine.CacheTypes;
import java.util.Objects;
import java.util.Set;
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

    public void evict(Long id) {
        Objects.requireNonNull(cacheManager.getCache(CacheTypes.PRESS.getName())).evict(id);
    }

    public void evictBatch(Set<String> ids) {
        ids.forEach(id ->
                Objects.requireNonNull(cacheManager.getCache(CacheTypes.PRESS.getName()))
                        .evict(Long.parseLong(id))
        );
    }

}
