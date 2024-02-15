package com.hh.mirishop.activity.comment.repository;

import com.hh.mirishop.activity.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.post.postId = :postId")
    List<Comment> findByPostId(@Param("postId") Long postId);

    @Query("SELECT c FROM Comment c WHERE c.parentComment.id = :parentCommentId")
    List<Comment> findByParentCommentId(@Param("parentCommentId") Long parentCommentId);

    @Query("SELECT c.post.postId FROM Comment c WHERE c.commentId = :commentId")
    Optional<Long> findPostIdByCommentId(@Param("commentId") Long commentId);

    @Query("SELECT c.commentId FROM Comment c WHERE c.memberNumber = :memberNumber")
    List<Long> findCommentIdsByMemberNumber(@Param("memberNumber") Long memberNumber);
}
