package com.a301.newsseug.domain.article.model.dto.response;

import com.a301.newsseug.domain.article.model.dto.ArticleDetailDto;
import com.a301.newsseug.domain.article.model.entity.Article;
import com.a301.newsseug.domain.reaction.model.dto.ReactionDetailDto;
import com.a301.newsseug.domain.press.model.dto.response.GetPressSummaryResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
@Schema(name = "기사 상세 정보", description = "기사 조회시 노출할 정보.")
public record GetArticleDetailResponseDto(

        @Schema(description = "기사 정보", examples = {"Object"})
        ArticleDetailDto article,

        @Schema(description = "언론사 정보", examples = {"Object"})
        GetPressSummaryResponseDto press,

        @Schema(description = "좋아요 유무와 개수", examples = {"Object"})
        ReactionDetailDto likeDetail,

        @Schema(description = "싫어요 유무와 개수", examples = {"Object"})
        ReactionDetailDto hateDetail

) {

    public static GetArticleDetailResponseDto of(
            Article article, Long currentViewCount, Boolean isSubscribed, ReactionDetailDto likeDetail, ReactionDetailDto hateDetail
    ) {
        return GetArticleDetailResponseDto.builder()
                .article(ArticleDetailDto.of(article, currentViewCount))
                .press(GetPressSummaryResponseDto.of(article.getPress(), isSubscribed))
                .likeDetail(likeDetail)
                .hateDetail(hateDetail)
                .build();
    }
}
