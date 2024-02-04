package com.hh.mirishop.common.exception;


import lombok.Getter;

@Getter
public class JwtTokenException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public JwtTokenException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}
