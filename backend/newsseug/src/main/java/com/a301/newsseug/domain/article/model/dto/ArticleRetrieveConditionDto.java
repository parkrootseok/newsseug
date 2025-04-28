package com.a301.newsseug.domain.article.model.dto;

import com.a301.newsseug.domain.article.model.entity.type.CategoryType;
import com.a301.newsseug.domain.article.model.entity.type.ConversionStatus;
import com.a301.newsseug.global.model.entity.ActivationStatus;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ArticleRetrieveConditionDto(
        CategoryType category,
        ActivationStatus activationStatus,
        ConversionStatus conversionStatus,
        LocalDateTime startDate,
        LocalDateTime endDate
) {

    public Optional<CategoryType> optionalCategory() {
        return Optional.ofNullable(category);
    }

    public Optional<LocalDateTime> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public Optional<LocalDateTime> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public static ArticleRetrieveConditionDto of(
            ActivationStatus activationStatus, ConversionStatus conversionStatus, LocalDateTime start, LocalDateTime end
    ) {
        return ArticleRetrieveConditionDto.builder()
                .activationStatus(activationStatus)
                .conversionStatus(conversionStatus)
                .startDate(start)
                .endDate(end)
                .build();
    }

    public static ArticleRetrieveConditionDto of(
            CategoryType category, ActivationStatus activationStatus, ConversionStatus conversionStatus, LocalDateTime start, LocalDateTime end
    ) {
        return ArticleRetrieveConditionDto.builder()
                .category(category)
                .activationStatus(activationStatus)
                .conversionStatus(conversionStatus)
                .startDate(start)
                .endDate(end)
                .build();
    }

}
