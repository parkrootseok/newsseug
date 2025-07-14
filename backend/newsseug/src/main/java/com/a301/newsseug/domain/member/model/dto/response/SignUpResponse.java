package com.a301.newsseug.domain.member.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
@Schema(name = "회원 가입 응답")
public record SignUpResponse(Long memberId) {
        public static SignUpResponse of(Long memberId) {
                return SignUpResponse.builder()
                        .memberId(memberId)
                        .build();
        }
}
