package com.hh.mirishop.post.service;

import com.hh.mirishop.auth.domain.UserDetailsImpl;
import com.hh.mirishop.common.exception.ErrorCode;
import com.hh.mirishop.common.exception.MemberException;
import com.hh.mirishop.common.exception.PostException;
import com.hh.mirishop.like.domain.LikeType;
import com.hh.mirishop.like.repository.LikeRepository;
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

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;

    @Transactional
    public Long createPost(PostRequest postRequest, Long currentMemberNumber) {
        Member currentMember = memberRepository.findById(currentMemberNumber)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        Post post = new Post(postRequest.getTitle(), postRequest.getContent(), currentMember);
        postRepository.save(post);

        return post.getPostId();
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getAllpostsByMember(int page, int size, UserDetailsImpl userDetails) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Long currentMemberNumber = userDetails.getNumber(); // 현재 사용자 ID 추출
        Page<Post> posts = postRepository.findByMemberNumberAndIsDeletedFalse(currentMemberNumber, pageable);

        return posts.map(post -> {
            int likeCounts = likeRepository.countByItemIdAndLikeType(post.getPostId(), LikeType.POST);
            return new PostResponse(post, likeCounts);
        });
    }

    public PostResponse getPost(Long postId) {
        Post post = findPostById(postId);
        int countPostLikes = countLikeForPost(postId);
        return new PostResponse(post, countPostLikes);
    }

    public Integer countLikeForPost(Long postId) {
        return likeRepository.countByItemIdAndLikeType(postId, LikeType.POST);
    }

    @Transactional
    public void updatePost(Long postId, PostRequest postRequest, Long currentMemberNumber) {
        Post post = findPostById(postId);

        checkAuthorizedMember(currentMemberNumber, post);

        post.update(postRequest.getTitle(), postRequest.getContent());
        postRepository.save(post);
    }

    private Post findPostById(Long postId) {
        return postRepository.findByPostIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
    }

    @Transactional
    public void deletePost(Long postId, Long currentMemberNumber) {
        Post post = findPostById(postId);

        checkAuthorizedMember(currentMemberNumber, post);

        post.delete(true);
        postRepository.save(post);
    }

    private void checkAuthorizedMember(Long currentMemberNumber, Post post) {
        if (!post.getMember().getNumber().equals(currentMemberNumber)) {
            throw new PostException(ErrorCode.UNAUTHORIZED_POST_ACCESS);
        }
    }
}
