package com.a301.newsseug.external.jwt.service;

import com.a301.newsseug.external.jwt.error.JwtTokenException;
import com.a301.newsseug.external.jwt.error.enums.JwtTokenErrorCode;
import com.a301.newsseug.external.jwt.repository.JwtTokenRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisJwtRefreshTokenStore {

    private final JwtTokenRepository jwtTokenRepository;

    public void store(Long memberId, String refreshToken, Duration duration) {
        jwtTokenRepository.saveWithTTL(String.valueOf(memberId), refreshToken, duration);
    }

    public String getTokenByMemberId(Long memberId) {
        return jwtTokenRepository.findByKey(String.valueOf(memberId))
                .orElseThrow(() -> new JwtTokenException(JwtTokenErrorCode.TOKEN_EXPIRED));
    }

    public Boolean invalidateTokenByMemberId(Long memberId) {
        return jwtTokenRepository.deleteByKey(String.valueOf(memberId));
    }

}
