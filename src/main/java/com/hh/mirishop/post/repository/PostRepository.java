package com.hh.mirishop.post.repository;

import com.hh.mirishop.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByPostIdAndIsDeletedFalse(Long postId, Pageable pageable);
    Optional<Post> findByPostIdAndIsDeletedFalse(Long postId);
}