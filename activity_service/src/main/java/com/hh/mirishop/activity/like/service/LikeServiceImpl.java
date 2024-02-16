package com.hh.mirishop.activity.like.service;

import com.hh.mirishop.activity.like.domain.LikeType;
import com.hh.mirishop.activity.client.UserFeignClient;
import com.hh.mirishop.activity.comment.repository.CommentRepository;
import com.hh.mirishop.activity.common.exception.CommentException;
import com.hh.mirishop.activity.common.exception.ErrorCode;
import com.hh.mirishop.activity.common.exception.LikeException;
import com.hh.mirishop.activity.common.exception.PostException;
import com.hh.mirishop.activity.like.entity.Like;
import com.hh.mirishop.activity.like.repository.LikeRepository;
import com.hh.mirishop.activity.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final UserFeignClient userFeignClient;

    @Override
    @Transactional
    public void likePost(Long postId, Long currentMemberNumber) {
        userFeignClient.findMemberByNumber(currentMemberNumber);
        findPost(postId);

        if (isAlreadyPostLiked(postId, currentMemberNumber)) {
            throw new LikeException(ErrorCode.ALREADY_LIKE);
        }

        Like like = likeRepository.save(new Like(currentMemberNumber, postId, LikeType.POST));
        // newsFeedService.createActivityForLike(like);
        // TODO: newsfeed 서비스에도 등록해야함
    }

    @Override
    @Transactional
    public void unlikePost(Long postId, Long currentMemberNumber) {
        userFeignClient.findMemberByNumber(currentMemberNumber);
        findPost(postId);
        Optional<Like> likeOpt = likeRepository.findByItemIdAndLikeTypeAndMemberNumber(postId, LikeType.POST,
                currentMemberNumber);

        if (likeOpt.isEmpty()) {
            throw new LikeException(ErrorCode.NOT_LIKE);
        }
        Like like = likeOpt.get();
        likeRepository.delete(like);
        // newsFeedService.deleteActivityForUnlike(like);
        // TODO: newsfeed 서비스에도 등록해야함
    }

    @Override
    @Transactional
    public void likeComment(Long commentId, Long currentMemberNumber) {
        userFeignClient.findMemberByNumber(currentMemberNumber);
        findComment(commentId);

        if (isAlreadyCommentLiked(commentId, currentMemberNumber)) {
            throw new LikeException(ErrorCode.ALREADY_LIKE);
        }
        Like like = likeRepository.save(new Like(currentMemberNumber, commentId, LikeType.COMMENT));
        // newsFeedService.createActivityForLike(like);
        // TODO: newsfeed 서비스에도 등록해야함
    }

    @Override
    @Transactional
    public void unlikeComment(Long commentId, Long currentMemberNumber) {
        userFeignClient.findMemberByNumber(currentMemberNumber);
        findComment(commentId);
        Optional<Like> likeOpt = likeRepository.findByItemIdAndLikeTypeAndMemberNumber(commentId, LikeType.COMMENT,
                currentMemberNumber);

        if (likeOpt.isEmpty()) {
            throw new LikeException(ErrorCode.NOT_LIKE);
        }
        Like like = likeOpt.get();
        likeRepository.delete(like);
        // newsFeedService.deleteActivityForUnlike(like);
        // TODO: newsfeed 서비스에도 등록해야함
    }

    @Override
    @Transactional(readOnly = true)
    public Long findPostIdByCommentId(Long commentId) {
        return commentRepository.findPostIdByCommentId(commentId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    private void findPost(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    private boolean isAlreadyPostLiked(Long postId, Long currentMemberNumber) {
        return likeRepository.existsByItemIdAndLikeTypeAndMemberNumber(postId, LikeType.POST, currentMemberNumber);
    }

    @Transactional(readOnly = true)
    private void findComment(Long commentId) {
        commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    private boolean isAlreadyCommentLiked(Long commentId, Long currentMemberNumber) {
        return likeRepository.existsByItemIdAndLikeTypeAndMemberNumber(commentId, LikeType.COMMENT,
                currentMemberNumber);
    }
}
