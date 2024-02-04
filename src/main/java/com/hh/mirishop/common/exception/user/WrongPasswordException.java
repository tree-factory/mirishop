package com.hh.mirishop.common.exception.user;

public class WrongPasswordException extends IllegalArgumentException{
    private static final String MESSAGE = "기존 비밀번호가 일치하지 않습니다.";

    public WrongPasswordException() {
        super(MESSAGE);
    }
}
