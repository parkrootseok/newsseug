package com.a301.newsseug.external.oauth.handler;

import com.a301.newsseug.domain.auth.usecase.AuthUseCase;
import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.domain.member.model.entity.type.RoleType;
import com.a301.newsseug.external.jwt.model.dto.JwtTokenPair;
import com.a301.newsseug.external.jwt.model.entity.enums.JwtTokenType;
import com.a301.newsseug.external.oauth.model.entity.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthUseCase authUseCase;

    @Value("${app.client.base-url}")
    private String url;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication
    ) throws IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Member member = oAuth2User.getMember();
        JwtTokenPair jwtTokenPair = authUseCase.login(member.getOAuth2Details().getProviderId());

        if (RoleType.ROLE_GUEST.equals(member.getRole())) {
            response.addHeader(JwtTokenType.ACCESS_TOKEN.getValue(), jwtTokenPair.accessToken().value());
            String redirectUrl = UriComponentsBuilder.fromUriString(url)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            response.sendRedirect(redirectUrl);
        } else {
            response.addHeader(JwtTokenType.ACCESS_TOKEN.getValue(), jwtTokenPair.accessToken().value());
            response.addHeader(JwtTokenType.REFRESH_TOKEN.getValue(), jwtTokenPair.refreshToken().value());
            String redirectUrl = UriComponentsBuilder.fromUriString(url)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            response.sendRedirect(redirectUrl);
        }
    }

}
