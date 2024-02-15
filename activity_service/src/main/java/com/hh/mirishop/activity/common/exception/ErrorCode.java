package com.hh.mirishop.activity.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // follow 에러 관련
    CANNOT_FOLLOW_SELF(HttpStatus.BAD_REQUEST,"자기 자신은 팔로우 할 수 없습니다."),
    FOLLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "팔로우 유저를 찾을 수 없습니다."),
    DUPLICATE_FOLLOW(HttpStatus.CONFLICT,"이미 팔로우한 유저는 팔로우 할 수 없습니다."),

    // email 에러 관련
    DIRECTORY_CREATION_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR,"이미지 저장 폴더 생성 실패"),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    EMPTY_FILE_EXCEPTION(HttpStatus.BAD_REQUEST, "업로드할 이미지가 없습니다."),
    INVALID_EMAIL_FROM(HttpStatus.BAD_REQUEST, "올바른 이메일 형식이 아닙니다."),
    UNSUPPORTED_IMAGE_FORMAT(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 이미지 형식입니다."),

    // token 에러 관련
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰값이 유효하지 않습니다."),

    // member 에러 관련
    DUPLICATED_PASSWORD(HttpStatus.BAD_REQUEST,"입력한 비밀번호가 기존 비밀번호와 같습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),
    INVALID_PASSWORD_LENGTH(HttpStatus.BAD_REQUEST, "비밀번호는 8자리 이상 입력해주세요."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "기존 비밀번호가 일치하지 않습니다."),

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
