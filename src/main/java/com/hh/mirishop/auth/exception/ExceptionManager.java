package com.hh.mirishop.auth.exception;

import com.hh.mirishop.auth.api.Response;
import com.hh.mirishop.member.exception.MemberException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(MemberException.class)
    public ResponseEntity<?> memberExceptionHandler(MemberException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(Response.error(e.getErrorCode().getMessage()));
    }
}
