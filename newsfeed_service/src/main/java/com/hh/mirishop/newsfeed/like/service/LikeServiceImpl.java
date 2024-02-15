package com.hh.newsfeed.like.service;

import com.hh.newsfeed.comment.repository.CommentRepository;
import com.hh.newsfeed.common.exception.CommentException;
import com.hh.newsfeed.common.exception.ErrorCode;
import com.hh.newsfeed.common.exception.LikeException;
import com.hh.newsfeed.common.exception.MemberException;
import com.hh.newsfeed.common.exception.PostException;
import com.hh.newsfeed.like.domain.LikeType;
import com.hh.newsfeed.like.entity.Like;
import com.hh.newsfeed.like.repository.LikeRepository;
import com.hh.newsfeed.member.entity.Member;
import com.hh.newsfeed.member.repository.MemberRepository;
import com.hh.newsfeed.newsfeed.service.NewsFeedService;
import com.hh.newsfeed.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final NewsFeedService newsFeedService;

    @Override
    @Transactional
    public void likePost(Long postId, Long currentMemberNumber) {
        Member currentMember = findMember(currentMemberNumber);
        findPost(postId);

        if (isAlreadyPostLiked(postId, currentMember)) {
            throw new LikeException(ErrorCode.ALREADY_LIKE);
        }

        Like like = likeRepository.save(new Like(currentMember, postId, LikeType.POST));
        newsFeedService.createActivityForLike(like);
    }

    @Override
    @Transactional
    public void unlikePost(Long postId, Long currentMemberNumber) {
        Member currentMember = findMember(currentMemberNumber);
        findPost(postId);
        Optional<Like> likeOpt = likeRepository.findByItemIdAndLikeTypeAndMember(postId, LikeType.POST, currentMember);

        if (likeOpt.isEmpty()) {
            throw new LikeException(ErrorCode.NOT_LIKE);
        }
        Like like = likeOpt.get();
        likeRepository.delete(like);
        newsFeedService.deleteActivityForUnlike(like);
    }

    @Override
    @Transactional
    public void likeComment(Long commentId, Long currentMemberNumber) {
        Member currentMember = findMember(currentMemberNumber);
        findComment(commentId);

        if (isAlreadyCommentLiked(commentId, currentMember)) {
            throw new LikeException(ErrorCode.ALREADY_LIKE);
        }
        Like like = likeRepository.save(new Like(currentMember, commentId, LikeType.COMMENT));
        newsFeedService.createActivityForLike(like);
    }

    @Override
    @Transactional
    public void unlikeComment(Long commentId, Long currentMemberNumber) {
        Member currentMember = findMember(currentMemberNumber);
        findComment(commentId);
        Optional<Like> likeOpt = likeRepository.findByItemIdAndLikeTypeAndMember(commentId, LikeType.COMMENT, currentMember);

        if (likeOpt.isEmpty()) {
            throw new LikeException(ErrorCode.NOT_LIKE);
        }
        Like like = likeOpt.get();
        likeRepository.delete(like);
        newsFeedService.deleteActivityForUnlike(like);
    }

    @Override
    @Transactional(readOnly = true)
    public Long findPostIdByCommentId(Long commentId) {
        return commentRepository.findPostIdByCommentId(commentId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
    }

    private Member findMember(Long currentMemberNumber) {
        return memberRepository.findById(currentMemberNumber)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    private void findPost(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
    }

    private boolean isAlreadyPostLiked(Long postId, Member currentMember) {
        return likeRepository.existsByItemIdAndLikeTypeAndMember(postId, LikeType.POST, currentMember);
    }

    private void findComment(Long commentId) {
        commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private boolean isAlreadyCommentLiked(Long commentId, Member currentMember) {
        return likeRepository.existsByItemIdAndLikeTypeAndMember(commentId, LikeType.COMMENT, currentMember);
    }
}
