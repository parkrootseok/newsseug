package com.a301.newsseug.domain.article.model.dto;

import com.a301.newsseug.domain.article.model.entity.Article;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Schema(name = "기사")
@Builder(access = AccessLevel.PRIVATE)
public record ArticleDetailDto(

        @Schema(description = "기사 고유 식별자", examples = {"99"})
        Long id,

        @Schema(description = "기사 제목", examples = {"오늘밤 최대 150㎜ 퍼붓는다 남부·동해안 '뒤끝 폭우' 고비"})
        String title,

        @Schema(description = "조회수", examples = {"99"})
        Long viewCount,

        @Schema(description = "출처 URL", examples = {"source-url"})
        String sourceUrl,

        @Schema(description = "영상 URL", examples = {"video-url"})
        String videoUrl,

        @Schema(description = "생성일", examples = {"20240423"})
        LocalDateTime createdAt

) {

    public static ArticleDetailDto of(Article article, Long currentViewCount) {
        return ArticleDetailDto.builder()
                .id(article.getId())
                .title(article.getTitle())
                .viewCount(currentViewCount)
                .sourceUrl(article.getSourceUrl())
                .videoUrl(article.getVideoUrl())
                .createdAt(article.getSourceCreatedAt())
                .build();
    }

}
