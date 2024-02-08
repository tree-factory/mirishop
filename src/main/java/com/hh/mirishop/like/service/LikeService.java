package com.hh.mirishop.like.service;

import com.hh.mirishop.comment.entity.Comment;
import com.hh.mirishop.comment.repository.CommentRepository;
import com.hh.mirishop.common.exception.CommentException;
import com.hh.mirishop.common.exception.ErrorCode;
import com.hh.mirishop.common.exception.LikeException;
import com.hh.mirishop.common.exception.MemberException;
import com.hh.mirishop.common.exception.PostException;
import com.hh.mirishop.like.domain.LikeType;
import com.hh.mirishop.like.entity.Like;
import com.hh.mirishop.like.repository.LikeRepository;
import com.hh.mirishop.member.entity.Member;
import com.hh.mirishop.member.repository.MemberRepository;
import com.hh.mirishop.post.entity.Post;
import com.hh.mirishop.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Transactional
    public void likePost(Long postId, Long currentMemberNumber) {
        Member currentMember = findMember(currentMemberNumber);
        Post post = findPost(postId);

        if (isAlreadyPostLiked(postId, currentMember)) {
            throw new LikeException(ErrorCode.ALREADY_LIKE);
        }
        likeRepository.save(new Like(currentMember, postId, LikeType.POST));

        /*
        뉴스피드에 대한 로직 추가
        */
    }

    private Member findMember(Long currentMemberNumber) {
        return memberRepository.findById(currentMemberNumber)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
    }

    private boolean isAlreadyPostLiked(Long postId, Member currentMember) {
        return likeRepository.existsByItemIdAndLikeTypeAndMember(postId, LikeType.POST, currentMember);
    }

    @Transactional
    public void unlikePost(Long postId, Long currentMemberNumber) {
        Member currentMember = findMember(currentMemberNumber);
        Post post = findPost(postId);
        Optional<Like> likeOpt = likeRepository.findByItemIdAndLikeTypeAndMember(postId, LikeType.POST, currentMember);

        if (likeOpt.isEmpty()) {
            throw new LikeException(ErrorCode.NOT_LIKE);
        }
        likeRepository.delete(likeOpt.get());
    }

    @Transactional
    public void likeComment(Long commentId, Long currentMemberNumber) {
        Member currentMember = findMember(currentMemberNumber);
        Comment comment = findComment(commentId);

        if (isAlreadyCommentLiked(commentId, currentMember)) {
            throw new LikeException(ErrorCode.ALREADY_LIKE);
        }
        likeRepository.save(new Like(currentMember, commentId, LikeType.COMMENT));

        /*
        뉴스피드에 대한 로직 추가
        */
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private boolean isAlreadyCommentLiked(Long commentId, Member currentMember) {
        return likeRepository.existsByItemIdAndLikeTypeAndMember(commentId, LikeType.COMMENT, currentMember);
    }

    @Transactional
    public void unlikeComment(Long commentId, Long currentMemberNumber) {
        Member currentMember = findMember(currentMemberNumber);
        Comment comment = findComment(commentId);
        Optional<Like> likeOpt = likeRepository.findByItemIdAndLikeTypeAndMember(commentId, LikeType.COMMENT, currentMember);

        if (likeOpt.isEmpty()) {
            throw new LikeException(ErrorCode.NOT_LIKE);
        }
        likeRepository.delete(likeOpt.get());
    }

}
