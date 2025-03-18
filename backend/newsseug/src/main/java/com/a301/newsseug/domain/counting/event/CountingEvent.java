package com.a301.newsseug.domain.counting.event;

import lombok.Builder;

@Builder
public record CountingEvent(String hash, Long id, Long delta) {

    public static CountingEvent of(String hash, Long id, Long delta) {
        return CountingEvent.builder()
                .hash(hash)
                .id(id)
                .delta(delta)
                .build();
    }

}
