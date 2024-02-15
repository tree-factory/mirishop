package com.hh.mirishop.activity.common.exception;

import lombok.Getter;

@Getter
public class LikeException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public LikeException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}

