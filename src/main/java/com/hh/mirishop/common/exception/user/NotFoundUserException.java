package com.hh.mirishop.common.exception.user;


public class NotFoundUserException extends RuntimeException {

    private static final String MESSAGE = "유저가 존재하지 않습니다.";

    public NotFoundUserException() {
        super(MESSAGE);
    }
}