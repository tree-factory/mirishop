package com.hh.mirishop.activity.common.exception;

import lombok.Getter;

@Getter
public class PostException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public PostException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
