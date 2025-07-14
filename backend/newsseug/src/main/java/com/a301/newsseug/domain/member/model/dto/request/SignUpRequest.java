package com.a301.newsseug.domain.member.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(name = "회원 가입 요청")
public record SignUpRequest(

        @Schema(description = "닉네임", defaultValue = "default")
        @NotBlank(message = "닉네임은 필수입니다.")
        String nickname,

        @Schema(description = "성별", defaultValue = "MALE", examples = {"MALE", "FEMALE"})
        @Pattern(regexp = "MALE|FEMALE", message = "성별은 MALE 또는 FEMALE이어야 합니다.")
        String gender,

        @Schema(description = "생년월일", defaultValue = "19971030", examples = {"19971030"})
        @Pattern(regexp = "\\d{8}", message = "생년월일은 yyyymmdd 형식의 8자리 숫자여야 합니다.")
        String birth,

        @Schema(description = "프로필 이미지 URL", defaultValue = "https://...")
        String profileImageUrl

) {}
