package com.a301.newsseug.domain.article.factory;

import com.a301.newsseug.domain.article.builder.ArticleSummaryDtoTestBuilder;
import com.a301.newsseug.domain.article.model.dto.ArticleSummaryDto;

import java.time.LocalDateTime;

public final class ArticleSummaryDtoFactory {

    private ArticleSummaryDtoFactory() {}

    public static ArticleSummaryDto summary(Long id, String title) {
        return ArticleSummaryDtoTestBuilder.aSummaryDto()
                .id(id)
                .title(title)
                .build();
    }

    public static ArticleSummaryDto summaryWithPress(Long id, String title, Long pressId) {
        return ArticleSummaryDtoTestBuilder.aSummaryDto()
                .id(id)
                .title(title)
                .pressId(pressId)
                .build();
    }

    public static ArticleSummaryDto summaryWithCounts(Long id, String title, Long pressId, Long viewCount) {
        return ArticleSummaryDtoTestBuilder.aSummaryDto()
                .id(id)
                .title(title)
                .pressId(pressId)
                .viewCount(viewCount)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
