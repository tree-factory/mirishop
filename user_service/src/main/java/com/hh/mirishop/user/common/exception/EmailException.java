package com.hh.mirishop.user.common.exception;

import lombok.Getter;

@Getter
public class EmailException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public EmailException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
