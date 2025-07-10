package com.a301.newsseug.domain.auth.error;

import com.a301.newsseug.global.error.BaseException;
import com.a301.newsseug.global.error.ErrorCode;

public class AuthException extends BaseException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
