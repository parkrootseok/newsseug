package com.a301.newsseug.global.execption;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

}

