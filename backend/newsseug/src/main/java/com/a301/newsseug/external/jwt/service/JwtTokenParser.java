package com.a301.newsseug.external.jwt.service;

import com.a301.newsseug.external.jwt.config.JwtTokenProperties;
import com.a301.newsseug.external.jwt.error.JwtTokenException;
import com.a301.newsseug.external.jwt.error.enums.JwtTokenErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenParser {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtTokenProperties jwtTokenProperties;

    public Header parseHeader(String token) {
        return executeJwtParsing(
                t -> getJwtParser().parseSignedClaims(t).getHeader(),
                token
        );
    }

    public Claims parseClaims(String token) {
        return executeJwtParsing(
                t -> getJwtParser().parseSignedClaims(t).getPayload(),
                token
        );
    }

    public String parseSubject(String token) {
        return executeJwtParsing(
                t -> getJwtParser().parseSignedClaims(t).getPayload().getSubject(),
                token
        );
    }

    private JwtParser getJwtParser() {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtTokenProperties.secret().getBytes(StandardCharsets.UTF_8)))
                .build();
    }

    private String removePrefix(String token) {
        if (!token.startsWith(TOKEN_PREFIX)) {
            throw new JwtTokenException(JwtTokenErrorCode.TOKEN_UNTRUSTWORTHY);
        }
        return token.substring(TOKEN_PREFIX.length());
    }

    private <T> T executeJwtParsing(JwtParsingFunction<T> function, String token) {
        try {
            return function.parse(removePrefix(token));
        } catch (ExpiredJwtException e) {
            throw new JwtTokenException(JwtTokenErrorCode.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new JwtTokenException(JwtTokenErrorCode.TOKEN_UNTRUSTWORTHY);
        }
    }

    @FunctionalInterface
    private interface JwtParsingFunction<T> {
        T parse(String token) throws JwtException;
    }

}
