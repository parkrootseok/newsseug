package com.a301.newsseug.domain.auth.controller;

import com.a301.newsseug.domain.auth.model.entity.CustomUserDetails;
import com.a301.newsseug.domain.auth.usecase.AuthUseCase;
import com.a301.newsseug.external.jwt.model.dto.JwtTokenPair;
import com.a301.newsseug.external.jwt.model.entity.enums.JwtTokenType;
import com.a301.newsseug.global.model.dto.Result;
import com.a301.newsseug.global.util.CookieUtil;
import com.a301.newsseug.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증/인가 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;

    @Operation(summary = "로그아웃 API", description = "로그아웃을 수행한다.")
    @GetMapping("/logout")
    public ResponseEntity<EntityModel<Result<Boolean>>> logout(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("memberId") @NotBlank Long memberId
    ) {
        return ResponseUtil.ok(
                Result.of(authUseCase.logout(userDetails.getMember(), memberId))
        );
    }

    @Operation(summary = "어세스 토큰 재발급 API", description = "어세스 토큰을 재발급한다.")
    @PostMapping("/reissue")
    public ResponseEntity<Result<Object>> issueAccessToken(
            HttpServletResponse httpServletResponse,
            @CookieValue(name = "refreshToken") String refreshToken
    ) {
        JwtTokenPair jwtTokenPair = authUseCase.reissue(refreshToken);
        httpServletResponse.addHeader(JwtTokenType.ACCESS_TOKEN.getValue(), jwtTokenPair.accessToken().value());
        httpServletResponse.addHeader(
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
        return ResponseUtil.created(Result.empty());
    }

}
