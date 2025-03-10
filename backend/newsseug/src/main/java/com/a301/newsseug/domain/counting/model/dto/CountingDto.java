package com.a301.newsseug.domain.counting.model.dto;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record CountingDto(
        Long articleId, long delta
) {

    public static CountingDto of(Long articleId, long delta) {
        return CountingDto.builder()
                .articleId(articleId)
                .delta(delta)
                .build();
    }

}