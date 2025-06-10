package com.a301.newsseug.domain.interaction.model.dto.response;

import com.a301.newsseug.domain.article.model.dto.response.GetArticleSummaryResponseDto;
import com.a301.newsseug.domain.press.model.dto.response.GetPressSummaryResponseDto;
import com.a301.newsseug.global.model.dto.SlicedResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record SearchResponse(

        @Schema(description = "언론사")
        List<GetPressSummaryResponseDto> press,

        @Schema(description = "기사")
        SlicedResponse<List<GetArticleSummaryResponseDto>> articles

) {

    public static SearchResponse of(
            List<GetPressSummaryResponseDto> press, SlicedResponse<List<GetArticleSummaryResponseDto>> articles
    ) {
        return SearchResponse.builder()
                .press(press)
                .articles(articles)
                .build();
    }

}
