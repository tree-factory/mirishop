package com.hh.mirishop.activity.post.dto;

import com.hh.mirishop.activity.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@AllArgsConstructor
public class PostResponse {

    private Long postId;
    private String title;
    private String content;
    private Long authorId;
    private int likesCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostResponse(Post post, int likesCount) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.authorId = post.getMemberNumber();
        this.likesCount = likesCount;
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}