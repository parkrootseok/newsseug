package com.a301.newsseug.domain.auth.usecase;

import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.auth.service.CustomUserDetailsService;
import com.a301.newsseug.external.jwt.error.JwtTokenException;
import com.a301.newsseug.external.jwt.error.enums.JwtTokenErrorCode;
import com.a301.newsseug.external.jwt.model.dto.JwtTokenPair;
import com.a301.newsseug.external.jwt.model.entity.JwtToken;
import com.a301.newsseug.external.jwt.service.JwtTokenIssuer;
import com.a301.newsseug.external.jwt.service.JwtTokenParser;
import com.a301.newsseug.external.jwt.service.RedisJwtRefreshTokenStore;
import io.jsonwebtoken.Claims;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;
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
        JwtToken accessToken = jwtTokenIssuer.issueAccessToken(memberId);
        JwtToken refreshToken = jwtTokenIssuer.issueRefreshToken(memberId);
        redisJwtRefreshTokenStore.store(memberId, refreshToken.value(), refreshToken.duration());
        return JwtTokenPair.of(accessToken, refreshToken);
    }

    public JwtToken reissue(Long memberId, String tokenFromClient) {
        Optional<String> savedToken = redisJwtRefreshTokenStore.getTokenByMemberId(memberId);
        if (savedToken.isEmpty()) {
            throw new JwtTokenException(JwtTokenErrorCode.TOKEN_EXPIRED);
        }
        if (!isEqualToken(tokenFromClient, savedToken.get())) {
            throw new JwtTokenException(JwtTokenErrorCode.TOKEN_UNTRUSTWORTHY);
        }
        return jwtTokenIssuer.issueAccessToken(memberId);
    }

    private boolean isEqualToken(String token, String savedToken) {
        return MessageDigest.isEqual(
                token.getBytes(StandardCharsets.UTF_8),
                savedToken.getBytes(StandardCharsets.UTF_8)
        );
    }

    public Boolean logout(Long memberId) {
        return redisJwtRefreshTokenStore.invalidateTokenByMemberId(memberId);
    }

    public void registerAuthenticatedUser(String subject) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(subject);
        UsernamePasswordAuthenticationToken authentication
                = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
