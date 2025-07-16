package com.a301.newsseug.external.jwt.usecase;

import com.a301.newsseug.external.jwt.service.JwtTokenParser;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUseCase {

    private final JwtTokenParser jwtTokenParser;

    public Claims extractClaims(String token) {
        return jwtTokenParser.parseClaims(token);
    }

}
