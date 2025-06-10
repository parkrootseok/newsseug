package com.a301.newsseug.domain.auth.exception;

import com.a301.newsseug.global.exception.BaseException;
import com.a301.newsseug.global.exception.ErrorCode;

public class RequiredLoginException extends BaseException {
    public RequiredLoginException() {
        super(ErrorCode.REQUIRED_LOGIN);
    }
}
