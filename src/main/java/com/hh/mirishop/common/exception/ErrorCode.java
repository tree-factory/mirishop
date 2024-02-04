package com.hh.mirishop.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    INVALID_EMAIL_FROM(HttpStatus.BAD_REQUEST, "올바른 이메일 형식이 아닙니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰값이 유효하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),
    INVALID_PASSWORD_LENGTH(HttpStatus.BAD_REQUEST, "비밀번호는 8자리 이상 입력해주세요."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    DUPLICATED_PASSWORD(HttpStatus.BAD_REQUEST,"입력한 비밀번호가 기존 비밀번호와 같습니다."),
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "기존 비밀번호가 일치하지 않습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;
}
