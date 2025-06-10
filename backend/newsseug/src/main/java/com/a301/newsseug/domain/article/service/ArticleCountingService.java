package com.a301.newsseug.domain.article.service;

import com.a301.newsseug.domain.counting.repository.CountRedisHashRepository;
import com.a301.newsseug.domain.counting.service.ViewCounterBuffer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleCountingService {

    private static final String VIEW_COUNT_KEY = "article:viewCount:";
    private static final String LIKE_COUNT_KEY = "article:count:";
    private static final String HATE_COUNT_KEY = "article:hateCount:";

    private final CountRedisHashRepository countRepository;
    private final ViewCounterBuffer viewCounterBuffer;

    public long increaseViewCount(Long articleId) {
        return viewCounterBuffer.increment(articleId) + getViewCount(articleId);
    }

    private Long getViewCount(Long articleId) {
        return countRepository.findByKey(VIEW_COUNT_KEY, articleId.toString()).orElse(0L);
    }

    public Long getLikeCount(Long articleId) {
        return countRepository.findByKey(LIKE_COUNT_KEY, articleId.toString()).orElse(0L);
    }

    public Long getHateCount(Long articleId) {
        return countRepository.findByKey(HATE_COUNT_KEY, articleId.toString()).orElse(0L);
    }

}
