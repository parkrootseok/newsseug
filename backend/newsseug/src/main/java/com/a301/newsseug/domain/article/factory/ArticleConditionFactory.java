package com.a301.newsseug.domain.article.factory;

import com.a301.newsseug.domain.article.model.dto.ArticleRetrieveCondition;
import com.a301.newsseug.domain.article.model.entity.type.CategoryType;
import com.a301.newsseug.domain.article.model.entity.type.ConversionStatus;
import com.a301.newsseug.domain.press.model.entity.Press;
import com.a301.newsseug.global.model.entity.ActivationStatus;
import java.time.LocalDateTime;
import java.util.Objects;

public class ArticleConditionFactory {

    public static ArticleRetrieveCondition create() {
        return create(null, null, null, null);
    }

    public static ArticleRetrieveCondition create(CategoryType category) {
        return create(null, category, null, null);
    }

    public static ArticleRetrieveCondition create(Press press, CategoryType category) {
        return create(press, category, null, null);
    }

    public static ArticleRetrieveCondition create(CategoryType category, LocalDateTime start, LocalDateTime end) {
        return create(null, category, start, end);
    }

    public static ArticleRetrieveCondition create(LocalDateTime start, LocalDateTime end) {
        return create(null, null, start, end);
    }

    public static ArticleRetrieveCondition create(Press press, CategoryType category, LocalDateTime start, LocalDateTime end) {
        boolean hasCategory = category != null && !CategoryType.ALL.equals(category);
        return ArticleRetrieveCondition.builder()
                .press(Objects.nonNull(press) ? press : null)
                .category(hasCategory ? category : null)
                .activationStatus(ActivationStatus.ACTIVE)
                .conversionStatus(ConversionStatus.SUCCESS)
                .startDate(start)
                .endDate(end)
                .build();
    }

}