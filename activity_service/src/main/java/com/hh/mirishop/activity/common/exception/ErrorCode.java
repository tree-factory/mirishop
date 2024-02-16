package com.hh.mirishop.activity.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // post 에러 관련
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다."),
    UNAUTHORIZED_POST_ACCESS(HttpStatus.UNAUTHORIZED, "게시글 수정/삭제는 회원 본인만 가능합니다."),

    // LIKE 에러 관련
    ALREADY_LIKE(HttpStatus.CONFLICT,"이미 좋아요를 누른 게시물입니다."),
    CORRESPOND_LIKE_TYPE(HttpStatus.BAD_REQUEST, "올바른 데이터 타입이 아닙니다."),
    NOT_LIKE(HttpStatus.CONFLICT, "좋아요를 누르지 않았습니다."),

    // COMMENT 에러 관련
    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "댓글이 존재하지 않습니다."),
    PARENT_COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "상위 댓글이 없습니다."),
    SUBCOMMENT_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "대댓글을 달 수 없습니다."),
    UNAUTHORIZED_COMMENT_ACCESS(HttpStatus.UNAUTHORIZED, "댓글 삭제는 회원 본인만 가능합니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
