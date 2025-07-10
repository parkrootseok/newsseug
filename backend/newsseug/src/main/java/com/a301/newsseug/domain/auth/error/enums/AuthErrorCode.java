package com.a301.newsseug.domain.auth.error.enums;

import com.a301.newsseug.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    AUTHENTICATION_MISMATCH(HttpStatus.FORBIDDEN, "현재 로그인한 사용자와 요청 정보가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return "[AUTH ERROR] " + message;
    }

}
