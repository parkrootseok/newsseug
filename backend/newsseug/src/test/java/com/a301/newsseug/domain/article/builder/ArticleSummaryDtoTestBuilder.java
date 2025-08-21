package com.a301.newsseug.domain.article.builder;

import com.a301.newsseug.domain.article.model.dto.ArticleSummaryDto;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class ArticleSummaryDtoTestBuilder {

    private static final AtomicLong SEQ = new AtomicLong(1L);

    private Long id = SEQ.getAndIncrement();
    private String thumbnailUrl = "http://thumbnail.url";
    private String title = "기사 제목";
    private Long viewCount = 0L;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Long pressId = 1L;

    private ArticleSummaryDtoTestBuilder() {}

    public static ArticleSummaryDtoTestBuilder aSummaryDto() {
        return new ArticleSummaryDtoTestBuilder();
    }

    public ArticleSummaryDtoTestBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ArticleSummaryDtoTestBuilder thumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }

    public ArticleSummaryDtoTestBuilder title(String title) {
        this.title = title;
        return this;
    }

    public ArticleSummaryDtoTestBuilder viewCount(Long viewCount) {
        this.viewCount = viewCount;
        return this;
    }

    public ArticleSummaryDtoTestBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public ArticleSummaryDtoTestBuilder pressId(Long pressId) {
        this.pressId = pressId;
        return this;
    }

    public ArticleSummaryDto build() {
        return new ArticleSummaryDto(id, thumbnailUrl, title, viewCount, createdAt, pressId);
    }

}
