package com.a301.newsseug.domain.press.builder;

import com.a301.newsseug.domain.press.model.dto.PressSummaryDto;

public class PressSummaryDtoTestBuilder {

    private Long id = 1L;
    private String name = "press";
    private String imageUrl = "imageUrl";
    private Long subscribeCount = 0L;

    private PressSummaryDtoTestBuilder() { }

    public static PressSummaryDtoTestBuilder aPressSummary() {
        return new PressSummaryDtoTestBuilder();
    }

    public PressSummaryDtoTestBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public PressSummaryDtoTestBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PressSummaryDtoTestBuilder imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public PressSummaryDtoTestBuilder subscribeCount(Long subscribeCount) {
        this.subscribeCount = subscribeCount;
        return this;
    }

    public PressSummaryDto build() {
        return new PressSummaryDto(this.id, this.name, this.imageUrl, this.subscribeCount);
    }

}
