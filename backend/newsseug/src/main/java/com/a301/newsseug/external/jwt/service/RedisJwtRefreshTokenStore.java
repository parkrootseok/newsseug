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

    public void store(String providerId, String refreshToken, Duration duration) {
        jwtTokenRepository.saveWithTTL(providerId, refreshToken, duration);
    }

    public String getTokenByMemberId(String providerId) {
        return jwtTokenRepository.findByKey(providerId)
                .orElseThrow(() -> new JwtTokenException(JwtTokenErrorCode.TOKEN_EXPIRED));
    }

    public Boolean invalidateTokenByMemberId(String providerId) {
        return jwtTokenRepository.deleteByKey(providerId);
    }

}
