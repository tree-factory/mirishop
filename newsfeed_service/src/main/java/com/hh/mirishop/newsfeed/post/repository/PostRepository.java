package com.hh.newsfeed.post.repository;

import com.hh.newsfeed.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByMemberNumberAndIsDeletedFalse(Long memberNumber, Pageable pageable);

    @Query("SELECT p.member.number FROM Post p WHERE p.member.number = :memberNumber")
    List<Long> findByMemberNumberAndIsDeletedFalse(@Param("memberNumber") Long memberNumber);

    Optional<Post> findByPostIdAndIsDeletedFalse(Long postId);
}