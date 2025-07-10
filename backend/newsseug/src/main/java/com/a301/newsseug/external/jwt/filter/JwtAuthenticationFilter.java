package com.a301.newsseug.external.jwt.filter;

import static com.a301.newsseug.global.constant.ErrorMessage.UNTRUSTWORTHY_TOKEN_MESSAGE;
import static com.a301.newsseug.global.constant.RegEx.EXCEPTION_URI_REGEX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.a301.newsseug.domain.auth.usecase.AuthUseCase;
import com.a301.newsseug.external.jwt.usecase.JwtUseCase;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
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
        if (Objects.isNull(request.getHeader(AUTHORIZATION))) {
            filterChain.doFilter(request, response);
            return;
        }

       try {
           String token = request.getHeader(AUTHORIZATION);
           if (jwtUseCase.verify(token)) {
               Claims claims = jwtUseCase.extractClaims(token);
               authUseCase.registerAuthenticatedUser(claims.getSubject());
           }
       } catch (JwtException e) {
           response.sendError(HttpServletResponse.SC_UNAUTHORIZED, UNTRUSTWORTHY_TOKEN_MESSAGE);
       } finally {
           filterChain.doFilter(request, response);
       }
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
