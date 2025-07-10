package com.a301.newsseug.domain.auth.controller;

import com.a301.newsseug.domain.auth.model.dto.response.ReissueTokenResponse;
import com.a301.newsseug.domain.auth.model.dto.response.LoginResponse;
import com.a301.newsseug.domain.auth.usecase.AuthUseCase;
import com.a301.newsseug.global.model.dto.Result;
import com.a301.newsseug.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증/인가 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthUseCase authUseCase;

    @Operation(summary = "로그인 API", description = "로그인을 수행한다.")
    @GetMapping("/login")
    public ResponseEntity<EntityModel<Result<LoginResponse>>> login(
            @RequestParam("providerId") Long providerId
    ) {
        return ResponseUtil.ok(
                Result.of(authUseCase.login(providerId))
        );
    }

    @Operation(summary = "로그아웃 API", description = "로그아웃을 수행한다.")
    @GetMapping("/logout")
    public ResponseEntity<EntityModel<Result<Boolean>>> logout(
            @RequestParam("providerId") Long providerId
    ) {
        return ResponseUtil.ok(
                Result.of(authUseCase.logout(providerId))
        );
    }

    @Operation(summary = "어세스 토큰 재발급 API", description = "어세스 토큰을 재발급한다.")
    @GetMapping("/reissue")
    public ResponseEntity<EntityModel<Result<ReissueTokenResponse>>> issueAccessToken(
            @RequestHeader("refresh-token") String refreshToken,
            @RequestParam("providerId") Long providerId
    ) {
        return ResponseUtil.ok(
                Result.of(authUseCase.reissue(refreshToken, providerId))
        );
    }

}
