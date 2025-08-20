package com.a301.newsseug.domain.press.factory;

import com.a301.newsseug.domain.press.model.dto.PressSummaryDto;
import com.a301.newsseug.domain.press.builder.PressSummaryDtoTestBuilder;


public final class PressSummaryDtoFactory {

    private PressSummaryDtoFactory() { }

    public static PressSummaryDto summary() {
        return PressSummaryDtoTestBuilder.aPressSummary().build();
    }

    public static PressSummaryDto summary(Long id, String name) {
        return PressSummaryDtoTestBuilder.aPressSummary()
                .id(id)
                .name(name)
                .build();
    }

    public static PressSummaryDto summary(Long id, String name, String imageUrl, Long subscribeCount) {
        return PressSummaryDtoTestBuilder.aPressSummary()
                .id(id)
                .name(name)
                .imageUrl(imageUrl)
                .subscribeCount(subscribeCount)
                .build();
    }
}
