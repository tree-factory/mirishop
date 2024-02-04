package com.hh.mirishop.common.exception.user;

public class SamePasswordException extends IllegalArgumentException{
    private static final String MESSAGE = "입력한 비밀번호가 기존 비밀번호와 같습니다.";

    public SamePasswordException() {
        super(MESSAGE);
    }
}