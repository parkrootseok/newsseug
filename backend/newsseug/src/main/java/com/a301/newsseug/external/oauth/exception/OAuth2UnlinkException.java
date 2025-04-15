package com.a301.newsseug.external.oauth.exception;

import static com.a301.newsseug.global.exception.ErrorCode.OAUTH_UNLINK;

import com.a301.newsseug.global.exception.BaseException;

public class OAuth2UnlinkException extends BaseException {
    public OAuth2UnlinkException() {
        super(OAUTH_UNLINK);
    }
}
