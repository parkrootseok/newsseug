package com.a301.newsseug.domain.article.factory;

import com.a301.newsseug.domain.article.builder.ArticleTestBuilder;
import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.article.model.entity.type.CategoryType;
import com.a301.newsseug.domain.article.model.entity.type.ConversionStatus;
import com.a301.newsseug.domain.press.model.entity.Press;

public class ArticleFactory {

    public static Article createDefault(Press press) {
        return ArticleTestBuilder.anArticle()
                .press(press)
                .build();
    }

    public static Article createWithCategory(Press press, CategoryType category) {
        return ArticleTestBuilder.anArticle()
                .press(press)
                .category(category)
                .build();
    }

    public static Article createWithTitle(Press press, String title) {
        return ArticleTestBuilder.anArticle()
                .press(press)
                .title(title)
                .build();
    }

    public static Article createWithStatus(Press press, ConversionStatus status) {
        return ArticleTestBuilder.anArticle()
                .press(press)
                .conversionStatus(status)
                .build();
    }

}
