package com.a301.newsseug.domain.counting.service;

import jakarta.annotation.PreDestroy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ViewCounterBuffer {

    private static final int THRESHOLD = 100;
    private static final String REDIS_VIEW_KEY = "article:viewCount:";

    private final CountingService countingService;

    // ConcurrentHashMap을 사용하여 멀티 스레드 환경에서도 Thread-safa 유지
    private final ConcurrentHashMap<Long, AtomicLong> buffer = new ConcurrentHashMap<>();

    @PreDestroy
    public void onShutDown() {
        // 서버가 종료되기 전 버퍼에 있는 값을 모두 반영
        flushAll();
    }

    public long increment(Long articleId) {
        long current = buffer
                .computeIfAbsent(articleId, id -> new AtomicLong(0))
                .incrementAndGet();

        // 현재 게시글에 대해서, 임계점을 넘으면 Redis에 반영
        if (THRESHOLD <= current) {
            flush(articleId);
        }

        return current;
    }

    public void flush(Long articleId) {
        AtomicLong counter = buffer.get(articleId);
        commitCounter(articleId, counter);
    }

    @Scheduled(cron = "*/30 * * * * ?")
    public void flushAll() {
        buffer.forEach((articleId, counter) -> {

            commitCounter(articleId, counter);

            // 메모리 누수 방지 : counter를 삭제하지 않으면, 사용하지 않는 객체도 메모리를 사용 중
            // [주의할 부분]
            // - flush와 flushAll이 같이 동작하면 반영하지 못한 값을 삭제할 수 있음
            // - 그렇기 때문에, 카운터가 0일 때만 삭제하도록 함
            if (counter.get() == 0) {
                buffer.remove(articleId, counter);
            }

        });
    }

    private void commitCounter(Long articleId, AtomicLong counter) {

        long delta = counter.getAndSet(0);

        if (delta <= 0) {
            return;
        }

        try {
            countingService.increment(REDIS_VIEW_KEY, articleId, delta);
        } catch (Exception e) {
            // Redis에 반영하지 못한 경우, 값을 복구
            log.warn("Redis increment 실패: articleId={}, delta={}", articleId, delta, e);
            counter.addAndGet(delta);
        }

    }

}
