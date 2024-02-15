package com.hh.mirishop.activity.common.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BaseResponse<T> {

    private final String message;
    private final Boolean success;
    private final T payload;

    public static <T> BaseResponse<T> of(String message, boolean success, T payload) {
        return new BaseResponse<>(message, success, payload);
    }
}
