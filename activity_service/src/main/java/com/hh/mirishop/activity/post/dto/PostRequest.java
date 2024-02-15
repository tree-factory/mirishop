package com.hh.mirishop.activity.post.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequest {

    @Size(max = 30, message = "제목은 최대 30자까지 작성 가능합니다.")
    private String title;

    private String content;
}