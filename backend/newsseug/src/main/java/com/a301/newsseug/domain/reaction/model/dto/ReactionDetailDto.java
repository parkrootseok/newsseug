package com.a301.newsseug.domain.reaction.model.dto;

import com.a301.newsseug.domain.reaction.model.entity.type.ReactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "리액션 정보", description = "단일 기사에 노출할 리액션 여부 및 개수 정보")
public record ReactionDetailDto(

        ReactionType type,

        Boolean isActivated,

        Long count

) {

    public static ReactionDetailDto of(ReactionType type, Boolean isEnabled, Long count) {
        return ReactionDetailDto.builder()
                .type(type)
                .isActivated(isEnabled)
                .count(count)
                .build();
    }

}
