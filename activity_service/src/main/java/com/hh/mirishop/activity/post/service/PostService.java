package com.hh.mirishop.activity.post.service;

import com.hh.mirishop.activity.post.dto.PostRequest;
import com.hh.mirishop.activity.post.dto.PostResponse;
import org.springframework.data.domain.Page;


public interface PostService {

    Long createPost(PostRequest postRequest, Long currentMemberNumber);

    Page<PostResponse> getAllpostsByMember(int page, int size, Long currentMemberNumber);

    PostResponse getPost(Long postId);

    void updatePost(Long postId, PostRequest postRequest, Long currentMemberNumber);

    void deletePost(Long postId, Long currentMemberNumber);
}
