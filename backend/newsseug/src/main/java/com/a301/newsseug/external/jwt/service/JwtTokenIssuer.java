package com.a301.newsseug.external.jwt.service;

import com.a301.newsseug.external.jwt.config.JwtTokenProperties;
import com.a301.newsseug.external.jwt.model.entity.JwtToken;
import com.a301.newsseug.external.jwt.model.entity.enums.JwtTokenType;
import com.a301.newsseug.global.util.ClockUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenIssuer {

    private final JwtTokenProperties jwtTokenProperties;

    public JwtToken issueAccessToken(Long memberId) {
        return JwtToken.of(
                JwtTokenType.ACCESS_TOKEN,
                issueToken(JwtTokenType.ACCESS_TOKEN, memberId, jwtTokenProperties.expiration().access()),
                jwtTokenProperties.accessTokenDuration()
        );
    }

    public JwtToken issueRefreshToken(Long memberId) {
        return JwtToken.of(
                JwtTokenType.REFRESH_TOKEN,
                issueToken(JwtTokenType.REFRESH_TOKEN, memberId, jwtTokenProperties.expiration().refresh()),
                jwtTokenProperties.refreshTokenDuration()
        );
    }

    private String issueToken(JwtTokenType type, Long memberId, long expiration) {
        LocalDateTime now = ClockUtil.getLocalDateTime();
        return Jwts.builder()
                .header()
                .add("type", type)
                .and()
                .subject(String.valueOf(memberId))
                .issuedAt(ClockUtil.convertToDate(now))
                .expiration(ClockUtil.getExpirationDate(now, expiration))
                .signWith(Keys.hmacShaKeyFor(jwtTokenProperties.secret().getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

}
