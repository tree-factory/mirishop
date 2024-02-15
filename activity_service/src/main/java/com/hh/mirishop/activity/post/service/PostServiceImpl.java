package com.hh.mirishop.activity.post.service;

import com.hh.mirishop.activity.like.domain.LikeType;
import com.hh.mirishop.activity.like.repository.LikeRepository;
import com.hh.mirishop.activity.post.dto.PostRequest;
import com.hh.mirishop.activity.post.dto.PostResponse;
import com.hh.mirishop.activity.post.repository.PostRepository;
import com.hh.mirishop.activity.client.UserFeignClient;
import com.hh.mirishop.activity.common.exception.ErrorCode;
import com.hh.mirishop.activity.common.exception.PostException;
import com.hh.mirishop.activity.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SoftDelete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final UserFeignClient userFeignClient;

    @Override
    @Transactional
    public Long createPost(PostRequest postRequest, Long currentMemberNumber) {
        userFeignClient.findMemberByNumber(currentMemberNumber);

        Post post = new Post(postRequest.getTitle(), postRequest.getContent(), currentMemberNumber);
        postRepository.save(post);

        return post.getPostId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponse> getAllpostsByMember(int page, int size, Long currentMemberNumber) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postRepository.findByMemberNumberAndIsDeletedFalse(currentMemberNumber, pageable);

        return posts.map(post -> {
            int likeCounts = likeRepository.countByItemIdAndLikeType(post.getPostId(), LikeType.POST);
            return new PostResponse(post, likeCounts);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPost(Long postId) {
        Post post = findPostById(postId);
        int countPostLikes = countLikeForPost(postId);
        return new PostResponse(post, countPostLikes);
    }

    @Override
    @Transactional
    public void updatePost(Long postId, PostRequest postRequest, Long currentMemberNumber) {
        Post post = findPostById(postId);

        checkAuthorizedMember(currentMemberNumber, post);

        post.update(postRequest.getTitle(), postRequest.getContent());
        postRepository.save(post);
    }

    @Override
    @SoftDelete
    @Transactional
    public void deletePost(Long postId, Long currentMemberNumber) {
        Post post = findPostById(postId);

        checkAuthorizedMember(currentMemberNumber, post);

        post.delete(true);
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    private Post findPostById(Long postId) {
        return postRepository.findByPostIdAndIsDeletedFalse(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
    }

    @Transactional(readOnly = true)

    private Integer countLikeForPost(Long postId) {
        return likeRepository.countByItemIdAndLikeType(postId, LikeType.POST);
    }

    @Transactional(readOnly = true)

    private void checkAuthorizedMember(Long currentMemberNumber, Post post) {
        if (!post.getMemberNumber().equals(currentMemberNumber)) {
            throw new PostException(ErrorCode.UNAUTHORIZED_POST_ACCESS);
        }
    }
}
