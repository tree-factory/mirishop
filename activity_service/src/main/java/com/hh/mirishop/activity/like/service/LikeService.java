package com.hh.mirishop.activity.like.service;

public interface LikeService {

    void likePost(Long postId, Long currentMemberNumber);

    void unlikePost(Long postId, Long currentMemberNumber);

    void likeComment(Long commentId, Long currentMemberNumber);

    void unlikeComment(Long commentId, Long currentMemberNumber);

    Long findPostIdByCommentId(Long commentId);
}
