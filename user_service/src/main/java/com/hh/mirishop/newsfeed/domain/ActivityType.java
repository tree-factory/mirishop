package com.hh.mirishop.newsfeed.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ActivityType {
    POST,
    COMMENT,
    LIKE
}
