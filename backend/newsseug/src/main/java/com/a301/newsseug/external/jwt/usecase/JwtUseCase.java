package com.a301.newsseug.external.jwt.usecase;

import com.a301.newsseug.external.jwt.error.JwtTokenException;
import com.a301.newsseug.external.jwt.error.enums.JwtTokenErrorCode;
import com.a301.newsseug.external.jwt.service.JwtTokenParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUseCase {

    private final JwtTokenParser jwtTokenParser;

    public Claims extractClaims(String token) {
        try {
            return jwtTokenParser.parseClaims(token);
        } catch (ExpiredJwtException e) {
            throw new JwtTokenException(JwtTokenErrorCode.TOKEN_EXPIRED);
        } catch (SignatureException | MalformedJwtException e) {
            throw new JwtTokenException(JwtTokenErrorCode.TOKEN_UNTRUSTWORTHY);
        }
    }

}
