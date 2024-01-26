package com.hh.mirishop.user.exception;

import lombok.Getter;

@Getter
public enum UserExceptionMessage {
    DUPLICATED_EMAIL("이미 존재하는 이메일입니다."),
    INVALID_EMAIL_FROM("올바른 이메일 형식이 아닙니다.");

    private String message;
    private static final String BASE_MESSAGE = "[ERROR] %s";

    UserExceptionMessage(String message) {
        this.message = String.format(BASE_MESSAGE, message);
    }
}
