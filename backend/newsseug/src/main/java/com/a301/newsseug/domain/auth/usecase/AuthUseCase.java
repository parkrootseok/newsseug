package com.a301.newsseug.domain.auth.usecase;

import com.a301.newsseug.domain.auth.model.dto.response.LoginResponse;
import com.a301.newsseug.domain.auth.model.dto.response.ReissueTokenResponse;
import com.a301.newsseug.domain.auth.service.CustomUserDetailsService;
import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.domain.member.repository.MemberRepository;
import com.a301.newsseug.external.jwt.error.JwtTokenException;
import com.a301.newsseug.external.jwt.error.enums.JwtTokenErrorCode;
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
    private final MemberRepository memberRepository;

    public LoginResponse login(Long providerId) {
        Member member = memberRepository.getOrThrow(String.valueOf(providerId));
        JwtToken accessToken = jwtTokenIssuer.issueAccessToken(providerId);
        JwtToken refreshToken = jwtTokenIssuer.issueRefreshToken(providerId);
        redisJwtRefreshTokenStore.store(providerId, refreshToken.value(), refreshToken.duration());
        return LoginResponse.of(accessToken.value(), refreshToken.value(), member.getIsFirst());
    }

    public Boolean logout(Long providerId) {
        return redisJwtRefreshTokenStore.invalidateTokenByMemberId(providerId);
    }

    public ReissueTokenResponse reissue(String tokenFromClient, Long providerId) {
        String savedToken = redisJwtRefreshTokenStore.getTokenByMemberId(providerId);
        if (!MessageDigest
                .isEqual(tokenFromClient.getBytes(StandardCharsets.UTF_8), savedToken.getBytes(StandardCharsets.UTF_8))
        ) {
            throw new JwtTokenException(JwtTokenErrorCode.TOKEN_UNTRUSTWORTHY);
        }
        return ReissueTokenResponse.of(jwtTokenIssuer.issueAccessToken(providerId).value());
    }

    public void registerAuthenticatedUser(String subject) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(subject);
        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
