package com.hh.mirishop.comment.repository;

import com.hh.mirishop.comment.entity.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId")
    List<Comment> findByPostId(@Param("postId") Long postId);

    @Query("SELECT c FROM Comment c WHERE c.parentComment.id = :parentCommentId")
    List<Comment> findByParentCommentId(@Param("ParentCommentId") Long parentCommentId);
}
