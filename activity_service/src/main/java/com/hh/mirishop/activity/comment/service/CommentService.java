package com.hh.mirishop.activity.comment.service;

import com.hh.mirishop.activity.comment.dto.CommentRequest;

import java.util.List;

public interface CommentService {

    Long createCommentOrReply(CommentRequest request, Long memberNumber, Long postId);

    void deleteComment(Long commentId, Long memberNumber);

    Long findPostIdByCommentId(Long commentId);

    List<Long> findCommentIdsByMemberNumber(Long memberNumber);
}

