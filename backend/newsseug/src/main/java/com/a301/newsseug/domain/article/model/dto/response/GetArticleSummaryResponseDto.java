package com.a301.newsseug.domain.article.model.dto.response;

import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.bookmark.model.entity.Bookmark;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(name = "기사 정보", description = "기사 목록 조회시 노출할 기사 정보")
public class GetArticleSummaryResponseDto {

    @Schema(description = "식별자", examples = {"99"})
    private Long id;

    @Schema(description = "언론사 이름", examples = {"조선일보"})
    private String pressName;

    @Schema(description = "썸네일", examples = {"https://{bucket-name}~amazonaws.com/{directory-name}/{thumbnail-url}"})
    private String thumbnailUrl;

    @Schema(description = "기사 제목", examples = {"오늘밤 최대 150㎜ 퍼붓는다 남부·동해안 '뒤끝 폭우' 고비"})
    private String title;

    @Schema(description = "조회수", examples = {"150"})
    private Long viewCount;

    @Schema(description = "생성일", examples = {"20240423"})
    private LocalDateTime createdAt;

    @Builder
    @QueryProjection
    public GetArticleSummaryResponseDto(Long id, String pressName, String thumbnailUrl, String title, Long viewCount, LocalDateTime createdAt) {
        this.id = id;
        this.pressName = pressName;
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
    }

    public static GetArticleSummaryResponseDto of(Article article) {
        return GetArticleSummaryResponseDto.builder()
                .id(article.getId())
                .pressName(article.getPress().getName())
                .thumbnailUrl(article.getThumbnailUrl())
                .title(article.getTitle())
                .viewCount(article.getViewCount())
                .createdAt(article.getSourceCreatedAt())
                .build();
    }

    public static List<GetArticleSummaryResponseDto> of(List<Article> article) {
        return article.stream()
                .map(GetArticleSummaryResponseDto::of)
                .toList();
    }

    public static List<GetArticleSummaryResponseDto> fromBookmark(List<Bookmark> bookmarks) {
        return bookmarks.stream()
                .map(bookmark -> GetArticleSummaryResponseDto.of(bookmark.getArticle()))
                .toList();
    }

}