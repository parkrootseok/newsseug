package com.a301.newsseug.domain.article.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleSummaryDto {

    private Long id;
    private String thumbnailUrl;
    private String title;
    private Long viewCount;
    private LocalDateTime createdAt;
    private Long pressId;

    @QueryProjection
    public ArticleSummaryDto(Long id, String thumbnailUrl, String title, Long viewCount, LocalDateTime createdAt, Long pressId) {
        this.id = id;
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.pressId = pressId;
    }

}
