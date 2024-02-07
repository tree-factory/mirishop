package com.hh.mirishop.comment.service;

import com.hh.mirishop.comment.dto.CommentRequest;
import com.hh.mirishop.post.entity.Post;

public interface CommentService {

    Long createComment(CommentRequest request, Long memberNumber, Long postId);

    Post findPostById(Long postId);

    void deleteComment(Long commentId, Long memberNumber);
}

