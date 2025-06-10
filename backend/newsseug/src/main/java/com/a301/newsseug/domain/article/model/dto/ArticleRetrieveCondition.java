package com.a301.newsseug.domain.article.model.dto;

import static com.a301.newsseug.domain.article.model.entity.QArticle.article;

import com.a301.newsseug.domain.article.model.entity.type.CategoryType;
import com.a301.newsseug.domain.article.model.entity.type.ConversionStatus;
import com.a301.newsseug.domain.press.model.entity.Press;
import com.a301.newsseug.global.model.entity.ActivationStatus;
import com.querydsl.core.BooleanBuilder;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Builder;

@Builder
public record ArticleRetrieveCondition(
        Press press,
        CategoryType category,
        ActivationStatus activationStatus,
        ConversionStatus conversionStatus,
        LocalDateTime startDate,
        LocalDateTime endDate
) {

    public BooleanBuilder toPredicate() {
        BooleanBuilder builder = new BooleanBuilder();
        if (Objects.nonNull(press)) {
            builder.and(article.press.eq(press));
        }
        if (Objects.nonNull(category) && !category.equals(CategoryType.ALL)) {
            builder.and(article.category.eq(category));
        }
        if (Objects.nonNull(startDate) && Objects.nonNull(endDate)) {
            builder.and(article.sourceCreatedAt.between(startDate, endDate));
        }
        builder.and(article.activationStatus.eq(ActivationStatus.ACTIVE));
        builder.and(article.conversionStatus.eq(ConversionStatus.SUCCESS));
        return builder;
    }

}
