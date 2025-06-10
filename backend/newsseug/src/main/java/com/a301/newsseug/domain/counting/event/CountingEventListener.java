package com.a301.newsseug.domain.counting.event;

import com.a301.newsseug.domain.counting.repository.CountRedisHashRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class CountingEventListener {

    private final CountRedisHashRepository countingService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCountingEvent(ReactionCountingEvent event) {
        countingService.increment(event.hash(), event.id(), event.delta());
    }

}
