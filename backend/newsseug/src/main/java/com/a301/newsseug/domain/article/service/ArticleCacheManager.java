package com.a301.newsseug.domain.article.service;

import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.article.repository.ArticleRepository;
import com.a301.newsseug.external.caffeine.CacheTypes;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleCacheManager {

    private final CacheManager cacheManager;
    private final ArticleRepository articleRepository;

    @Cacheable(value = "article", key = "#articleId")
    public Article getCachedArticle(Long articleId) {
        return articleRepository.findOrThrow(articleId);
    }

    public void evictArticleCache(Long articleId) {
        Objects.requireNonNull(cacheManager.getCache(CacheTypes.ARTICLE.getName())).evict(articleId);
    }

}
