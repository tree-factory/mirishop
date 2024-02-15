package com.hh.mirishop.activity.common.exception;

import lombok.Getter;

@Getter
public class CommentException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public CommentException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}