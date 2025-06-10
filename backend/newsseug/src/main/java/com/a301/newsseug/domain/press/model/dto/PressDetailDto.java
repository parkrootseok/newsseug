package com.a301.newsseug.domain.press.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(name = "언론사 상세 정보")
public class PressDetailDto {

    @Schema(description = "식별자")
    private Long id;

    @Schema(description = "언론사 명")
    private String name;

    @Schema(description = "언론사 로고 URL")
    private String imageUrl;

    @Schema(description = "언론사 소개")
    private String description;

    @Schema(description = "구독자 수")
    private Long subscribeCount;

    @Builder
    @QueryProjection
    public PressDetailDto(Long id, String name, String imageUrl, String description, Long subscribeCount) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.subscribeCount = subscribeCount;
    }

}