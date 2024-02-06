package com.hh.mirishop.post.service;

import com.hh.mirishop.auth.domain.UserDetailsImpl;
import com.hh.mirishop.common.exception.ErrorCode;
import com.hh.mirishop.common.exception.MemberException;
import com.hh.mirishop.common.exception.PostException;
import com.hh.mirishop.follow.repository.FollowRepository;
import com.hh.mirishop.member.entity.Member;
import com.hh.mirishop.member.repository.MemberRepository;
import com.hh.mirishop.post.dto.PostRequest;
import com.hh.mirishop.post.dto.PostResponse;
import com.hh.mirishop.post.entity.Post;
import com.hh.mirishop.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    @Transactional
    public Long createPost(PostRequest postRequest, Long currentMemberNumber) {
        Member currentMember = memberRepository.findById(currentMemberNumber)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));


        Post post = new Post(postRequest.getTitle(), postRequest.getContent(), currentMember);
        postRepository.save(post);

        return post.getPostId();
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getAllposts(@RequestParam("page") int page,
                                          @RequestParam("size") int size,
                                          UserDetailsImpl userDetails) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Long currentMemberNumber = userDetails.getNumber(); // 현재 사용자 ID 추출

        return postRepository.findByPostIdAndIsDeletedFalse(currentMemberNumber, pageable)
                .map(PostResponse::new);
    }

    @Transactional
    public void updatePost(Long postId, PostRequest postRequest, Long currentMemberNumber) {
        Post post = postRepository.findByPostIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));

        checkAuthorizedMember(currentMemberNumber, post);

        post.update(postRequest.getTitle(), postRequest.getContent());
        postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long postId, Long currentMemberNumber) {
        Post post = postRepository.findByPostIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));

        checkAuthorizedMember(currentMemberNumber, post);

        post.delete(true);
        postRepository.save(post);
    }

    private void checkAuthorizedMember(Long currentMemberNumber, Post post) {
        if (!post.getMember().getNumber().equals(currentMemberNumber)) {
            throw new PostException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
    }
}
