package com.a301.newsseug.domain.interaction.exception;

import com.a301.newsseug.global.exception.BaseException;
import com.a301.newsseug.global.exception.ErrorCode;

public class AlreadyExistReactionException extends BaseException {
    public AlreadyExistReactionException() {
        super(ErrorCode.ALREADY_EXIST_REACTION);
    }
}
