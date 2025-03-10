package com.a301.newsseug.domain.counting.service;

import com.a301.newsseug.domain.article.repository.ArticleRepository;
import com.a301.newsseug.domain.counting.model.dto.CountingDto;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountingSyncServiceImpl implements CountingSyncService {

    private final CountingService countingService;
    private final ArticleRepository articleRepository;

    @Override
    @Scheduled(cron = "0 0/7 * * * ?")
    public void scheduledSyncCounting() {
        syncCounting("article:likeCount:", "likeCount");
        syncCounting("article:hateCount:", "hateCount");
        syncCounting("article:viewCount:", "viewCount");
    }

    private synchronized void syncCounting(String redisHashKey, String field) {
        Map<Object, Object> countingLog = countingService.findByHash(redisHashKey);

        if (Objects.nonNull(countingLog) && !countingLog.isEmpty()) {
            countingLog.forEach((key, value) -> {
                Long articleId = Long.parseLong(String.valueOf(key));
                Number delta = (Number) value;

                if (Objects.nonNull(delta)) {
                    countingService.deleteByKey(redisHashKey, articleId);
                    articleRepository.updateCount(field, articleId, delta.longValue());
                    log.info("Updating articleId: {}, New {}: {}", articleId, field, delta);
                }
            });
        }
    }

}
