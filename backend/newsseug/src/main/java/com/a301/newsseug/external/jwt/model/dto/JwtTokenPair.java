package com.a301.newsseug.external.jwt.model.dto;

import com.a301.newsseug.external.jwt.model.entity.JwtToken;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record JwtTokenPair(
        JwtToken accessToken,
        JwtToken refreshToken
) {
    public static JwtTokenPair of(JwtToken accessToken, JwtToken refreshToken) {
        return JwtTokenPair.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
