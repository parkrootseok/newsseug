package com.a301.newsseug.domain.member.exception;

import com.a301.newsseug.global.exception.BaseException;
import com.a301.newsseug.global.exception.ErrorCode;

public class DuplicateNicknameException extends BaseException {
    public DuplicateNicknameException() {
        super(ErrorCode.DUPLICATED_NICKNAME);
    }
}
