package com.a301.newsseug.domain.auth.usecase;

import com.a301.newsseug.domain.auth.error.AuthException;
import com.a301.newsseug.domain.auth.error.enums.AuthErrorCode;
import com.a301.newsseug.domain.auth.model.dto.response.ReissueTokenResponse;
import com.a301.newsseug.domain.auth.service.CustomUserDetailsService;
import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.external.jwt.error.JwtTokenException;
import com.a301.newsseug.external.jwt.error.enums.JwtTokenErrorCode;
import com.a301.newsseug.external.jwt.model.dto.JwtTokenPair;
import com.a301.newsseug.external.jwt.model.entity.JwtToken;
import com.a301.newsseug.external.jwt.service.JwtTokenIssuer;
import com.a301.newsseug.external.jwt.service.RedisJwtRefreshTokenStore;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthUseCase {

    private final JwtTokenIssuer jwtTokenIssuer;
    private final CustomUserDetailsService customUserDetailsService;
    private final RedisJwtRefreshTokenStore redisJwtRefreshTokenStore;

    public JwtTokenPair login(Long memberId) {
        JwtToken accessToken = jwtTokenIssuer.generateAccessToken(memberId);
        JwtToken refreshToken = jwtTokenIssuer.generateRefreshToken(memberId);
        redisJwtRefreshTokenStore.store(memberId, refreshToken.value(), refreshToken.duration());
        return JwtTokenPair.of(accessToken, refreshToken);
    }

    public Boolean logout(Member member, Long memberId) {
        if (!member.getId().equals(memberId)) {
            throw new AuthException(AuthErrorCode.AUTHENTICATION_MISMATCH);
        }
        return redisJwtRefreshTokenStore.invalidateTokenByMemberId(memberId);
    }

    public ReissueTokenResponse reissue(String tokenFromClient, Long memberId) {
        String savedToken = redisJwtRefreshTokenStore.getTokenByMemberId(memberId);
        if (!MessageDigest
                .isEqual(tokenFromClient.getBytes(StandardCharsets.UTF_8), savedToken.getBytes(StandardCharsets.UTF_8))
        ) {
            throw new JwtTokenException(JwtTokenErrorCode.TOKEN_UNTRUSTWORTHY);
        }
        return ReissueTokenResponse.of(jwtTokenIssuer.generateAccessToken(memberId).value());
    }

    public void registerAuthenticatedUser(String subject) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(subject);
        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
