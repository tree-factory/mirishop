package com.hh.mirishop.like.domain;

import com.hh.mirishop.common.exception.ErrorCode;
import com.hh.mirishop.common.exception.LikeException;

import java.util.stream.Stream;

public enum LikeType {

    POST(1), COMMENT(2);

    int value;

    LikeType(int value) {
        this.value = value;
    }

    public int toDbValue() {
        return value;
    }

    public static LikeType from(Integer dbData) {
        return Stream.of(LikeType.values())
                .filter(x -> x.value == dbData)
                .findFirst()
                .orElseThrow(() -> new LikeException(ErrorCode.CORRESPOND_LIKE_TYPE));
    }
}
