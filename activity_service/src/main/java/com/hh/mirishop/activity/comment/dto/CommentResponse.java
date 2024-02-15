package com.hh.mirishop.activity.comment.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CommentResponse {

    private String nickName;
    private String content;
    private Long commentId;
    private Long parentCommentId;
    private int likeCount;
}
