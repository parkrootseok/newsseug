package com.a301.newsseug.external.jwt.filter;

import static com.a301.newsseug.global.constant.ErrorMessage.UNTRUSTWORTHY_TOKEN_MESSAGE;
import static com.a301.newsseug.global.constant.RegEx.EXCEPTION_URI_REGEX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.a301.newsseug.domain.auth.usecase.AuthUseCase;
import com.a301.newsseug.external.jwt.error.JwtTokenException;
import com.a301.newsseug.external.jwt.error.enums.JwtTokenErrorCode;
import com.a301.newsseug.external.jwt.usecase.JwtUseCase;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthUseCase authUseCase;
    private final JwtUseCase jwtUseCase;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {
        String token = request.getHeader(AUTHORIZATION);
        if (Objects.isNull(token)) {
            filterChain.doFilter(request, response);
            return;
        }
       Claims claims = jwtUseCase.extractClaims(token);
       authUseCase.registerAuthenticatedUser(claims.getSubject());
       filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        for (String regex : EXCEPTION_URI_REGEX) {
            if (request.getRequestURI().matches(regex)) {
                return true;
            }
        }
        return false;
    }

}
