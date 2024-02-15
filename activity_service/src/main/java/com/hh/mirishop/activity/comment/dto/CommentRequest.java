package com.hh.mirishop.activity.comment.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequest {

    @Size(min = 1, max = 400, message = "400자 이하로 입력해주세요.")
    private String content;
    private Long parentCommentId;
}
