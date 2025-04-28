package com.a301.newsseug.domain.article.util;

import com.a301.newsseug.domain.article.model.dto.ArticleRetrieveConditionDto;
import com.a301.newsseug.domain.article.model.entity.QArticle;
import com.a301.newsseug.domain.article.model.entity.type.CategoryType;
import com.querydsl.core.BooleanBuilder;

public class ArticleConditionBuilder {

    public static BooleanBuilder build(ArticleRetrieveConditionDto conditions) {

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(QArticle.article.activationStatus.eq(conditions.activationStatus()));
        builder.and(QArticle.article.conversionStatus.eq(conditions.conversionStatus()));

        conditions.optionalCategory()
                .filter(category -> category.equals(CategoryType.ALL))
                .ifPresent(category -> builder.and(QArticle.article.category.eq(category)));

        conditions.optionalStartDate()
                .ifPresent(date -> builder.and(QArticle.article.sourceCreatedAt.goe(date)));

        conditions.optionalEndDate()
                .ifPresent(date -> builder.and(QArticle.article.sourceCreatedAt.loe(date)));

        return builder;
    }

}
