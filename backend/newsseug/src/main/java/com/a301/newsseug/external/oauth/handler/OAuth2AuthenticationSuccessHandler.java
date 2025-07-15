package com.a301.newsseug.external.oauth.handler;

import com.a301.newsseug.domain.auth.usecase.AuthUseCase;
import com.a301.newsseug.domain.member.model.entity.Member;
import com.a301.newsseug.domain.member.model.entity.type.RoleType;
import com.a301.newsseug.external.jwt.model.dto.JwtTokenPair;
import com.a301.newsseug.external.jwt.model.entity.enums.JwtTokenType;
import com.a301.newsseug.external.oauth.model.entity.CustomOAuth2User;
import com.a301.newsseug.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthUseCase authUseCase;

    @Value("${app.client.base-url}")
    private String BASE_URL;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication
    ) throws IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        Member member = oAuth2User.getMember();

        JwtTokenPair jwtTokenPair = authUseCase.login(member.getId());
        response.addHeader(JwtTokenType.ACCESS_TOKEN.getValue(), jwtTokenPair.accessToken().value());
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                CookieUtil.create(
                        jwtTokenPair.refreshToken().type().name(),
                        jwtTokenPair.refreshToken().value(),
                        jwtTokenPair.refreshToken().duration(),
                        true,
                        true,
                        "None"
                ).toString()
        );

        if (RoleType.ROLE_GUEST.equals(member.getRole())) {
            String redirectUrl = generateRedirectUrl(BASE_URL + "/sign-up");
            response.sendRedirect(redirectUrl);
        } else {
            String redirectUrl = generateRedirectUrl(BASE_URL);
            response.sendRedirect(redirectUrl);
        }
    }

    private String generateRedirectUrl(String url) {
        return UriComponentsBuilder.fromUriString(url)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUriString();
    }

}
