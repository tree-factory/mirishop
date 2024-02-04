package com.hh.mirishop.common.exception;

import lombok.Getter;

@Getter
public class MemberException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public MemberException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
