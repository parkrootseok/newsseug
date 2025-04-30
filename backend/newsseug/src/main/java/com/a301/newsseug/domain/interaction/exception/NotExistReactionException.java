package com.a301.newsseug.domain.interaction.exception;

import com.a301.newsseug.global.exception.BaseException;
import com.a301.newsseug.global.exception.ErrorCode;

public class NotExistReactionException extends BaseException {
    public NotExistReactionException() {
        super(ErrorCode.NOT_EXIST_REACTION);
    }
}
