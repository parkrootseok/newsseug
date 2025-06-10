package com.a301.newsseug.domain.folder.model.dto.response;

import com.a301.newsseug.domain.article.model.dto.response.GetArticleSummaryResponseDto;
import com.a301.newsseug.domain.folder.model.entity.Folder;
import com.a301.newsseug.global.model.dto.SlicedResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
@Schema(name = "폴더 상세 정보", description = "폴더 목록에서 하나의 폴더를 클릭했을 때 노출할 정보.")
public record GetFolderDetailsResponse(

        @Schema(description = "식별자")
        Long id,

        @Schema(description = "이름")
        String title,

        @Schema(description = "스크랩한 기사 목록")
        SlicedResponse<List<GetArticleSummaryResponseDto>> articles

) {

    public static GetFolderDetailsResponse of(Folder folder, SlicedResponse<List<GetArticleSummaryResponseDto>> articles) {
        return GetFolderDetailsResponse.builder()
                .id(folder.getId())
                .title(folder.getTitle())
                .articles(articles)
                .build();
    }

}
