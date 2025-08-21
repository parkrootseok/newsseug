package com.a301.newsseug.domain.article.builder;

import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.article.model.entity.type.CategoryType;
import com.a301.newsseug.domain.article.model.entity.type.ConversionStatus;
import com.a301.newsseug.domain.press.model.entity.Press;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.atomic.AtomicLong;

public class ArticleTestBuilder {

    private static final AtomicLong SEQ = new AtomicLong(1L);

    private Long id = SEQ.getAndIncrement();
    private Press press;
    private String title = "title";
    private String sourceUrl = "http://source.url";
    private String contentUrl = "http://content.url";
    private String videoUrl = "http://video.url";
    private String thumbnailUrl = "http://thumbnail.url";
    private CategoryType category = CategoryType.POLITICS;
    private ConversionStatus conversionStatus = ConversionStatus.SUCCESS;

    private ArticleTestBuilder() {}

    public static ArticleTestBuilder anArticle() {
        return new ArticleTestBuilder();
    }

    public ArticleTestBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ArticleTestBuilder press(Press press) {
        this.press = press;
        return this;
    }

    public ArticleTestBuilder title(String title) {
        this.title = title;
        return this;
    }

    public ArticleTestBuilder sourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
        return this;
    }

    public ArticleTestBuilder contentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
        return this;
    }

    public ArticleTestBuilder videoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public ArticleTestBuilder thumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }

    public ArticleTestBuilder category(CategoryType category) {
        this.category = category;
        return this;
    }

    public ArticleTestBuilder conversionStatus(ConversionStatus status) {
        this.conversionStatus = status;
        return this;
    }

    public Article build() {
        Article article = Article.builder()
                .press(this.press)
                .title(this.title)
                .sourceUrl(this.sourceUrl)
                .contentUrl(this.contentUrl)
                .videoUrl(this.videoUrl)
                .thumbnailUrl(this.thumbnailUrl)
                .category(this.category)
                .build();

        ReflectionTestUtils.setField(article, "id", this.id);
        ReflectionTestUtils.setField(article, "conversionStatus", this.conversionStatus);

        return article;
    }

}
