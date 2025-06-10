package com.a301.newsseug.domain.press.model.dto.response;

import com.a301.newsseug.domain.press.model.dto.PressSummaryDto;
import com.a301.newsseug.domain.press.model.entity.Press;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
@Schema(name = "언론사 정보", description = "언론사 목록 조회 시 노출할 정보.")
public record GetPressSummaryResponseDto(

        @Schema(description = "식별자")
        Long id,

        @Schema(description = "이름")
        String name,

        @Schema(description = "언론사 로고 URL")
        String imageUrl,

        @Schema(description = "구독자수")
        Long subscribeCount,

        @Schema(description = "구독 여부")
        Boolean isSubscribed

) {

    public static GetPressSummaryResponseDto of(PressSummaryDto press) {
        return GetPressSummaryResponseDto.builder()
                .id(press.getId())
                .name(press.getName())
                .imageUrl(press.getImageUrl())
                .isSubscribed(false)
                .build();
    }

    public static GetPressSummaryResponseDto of(Press press, Boolean isSubscribed) {
        return GetPressSummaryResponseDto.builder()
                .id(press.getId())
                .name(press.getName())
                .imageUrl(press.getImageUrl())
                .isSubscribed(isSubscribed)
                .build();
    }

    public static GetPressSummaryResponseDto of(PressSummaryDto pressSummary, Boolean isSubscribed) {
        return GetPressSummaryResponseDto.builder()
            .id(pressSummary.getId())
            .name(pressSummary.getName())
            .imageUrl(pressSummary.getImageUrl())
            .isSubscribed(isSubscribed)
            .build();
    }

    public static List<GetPressSummaryResponseDto> of(List<PressSummaryDto> pressSummaries) {
        return pressSummaries.stream()
                .map(GetPressSummaryResponseDto::of)
                .toList();
    }

    public static List<GetPressSummaryResponseDto> of(List<PressSummaryDto> pressSummaries, Set<Press> subscribePress) {
        return pressSummaries.stream()
                .map(p -> GetPressSummaryResponseDto.of(p, subscribePress.contains(p)))
                .toList();
    }

}
