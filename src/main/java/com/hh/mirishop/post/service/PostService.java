package com.hh.mirishop.post.service;

import com.hh.mirishop.auth.domain.UserDetailsImpl;
import com.hh.mirishop.post.dto.PostRequest;
import com.hh.mirishop.post.dto.PostResponse;
import org.springframework.data.domain.Page;


public interface PostService {

    Long createPost(PostRequest postRequest, Long currentMemberNumber);

    Page<PostResponse> getAllpostsByMember(int page, int size, UserDetailsImpl userDetails);

    PostResponse getPost(Long postId);

    void updatePost(Long postId, PostRequest postRequest, Long currentMemberNumber);

    void deletePost(Long postId, Long currentMemberNumber);
}
