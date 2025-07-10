package com.a301.newsseug.external.jwt.error;

import com.a301.newsseug.global.error.BaseException;
import com.a301.newsseug.global.error.ErrorCode;

public class JwtTokenException extends BaseException {
    public JwtTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
