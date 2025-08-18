package com.a301.newsseug.domain.article.model.dto.response;

import com.a301.newsseug.domain.article.model.dto.ArticleSummaryDto;
import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.bookmark.model.entity.Bookmark;
import com.a301.newsseug.domain.press.model.entity.Press;
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

    @Schema(description = "썸네일", examples = {"https://{bucket-name}~amazonaws.com/{directory-name}/{thumbnail-url}"})
    private String thumbnailUrl;

    @Schema(description = "기사 제목", examples = {"오늘밤 최대 150㎜ 퍼붓는다 남부·동해안 '뒤끝 폭우' 고비"})
    private String title;

    @Schema(description = "조회수", examples = {"150"})
    private Long viewCount;

    @Schema(description = "생성일", examples = {"20240423"})
    private LocalDateTime createdAt;

    private String pressName;

    @Builder
    public GetArticleSummaryResponseDto(Long id, String thumbnailUrl, String title, Long viewCount, LocalDateTime createdAt, String pressName) {
        this.id = id;
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.pressName = pressName;
    }

    public static GetArticleSummaryResponseDto of(Article article) {
        return GetArticleSummaryResponseDto.builder()
                .id(article.getId())
                .thumbnailUrl(article.getThumbnailUrl())
                .title(article.getTitle())
                .viewCount(article.getViewCount())
                .createdAt(article.getCreatedAt())
                .pressName(article.getPress().getName())
                .build();
    }

    public static GetArticleSummaryResponseDto of(ArticleSummaryDto article, Press press) {
        return GetArticleSummaryResponseDto.builder()
                .id(article.getId())
                .thumbnailUrl(article.getThumbnailUrl())
                .title(article.getTitle())
                .viewCount(article.getViewCount())
                .createdAt(article.getCreatedAt())
                .pressName(press.getName())
                .build();
    }

    public static List<GetArticleSummaryResponseDto> fromBookmark(List<Bookmark> bookmarks) {
        return bookmarks.stream()
                .map(bookmark -> GetArticleSummaryResponseDto.of(bookmark.getArticle()))
                .toList();
    }

}
