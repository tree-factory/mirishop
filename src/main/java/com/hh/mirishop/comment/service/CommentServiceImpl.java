package com.hh.mirishop.comment.service;


import com.hh.mirishop.comment.dto.CommentRequest;
import com.hh.mirishop.comment.entity.Comment;
import com.hh.mirishop.comment.repository.CommentRepository;
import com.hh.mirishop.common.exception.CommentException;
import com.hh.mirishop.common.exception.ErrorCode;
import com.hh.mirishop.common.exception.MemberException;
import com.hh.mirishop.common.exception.PostException;
import com.hh.mirishop.member.entity.Member;
import com.hh.mirishop.member.repository.MemberRepository;
import com.hh.mirishop.post.entity.Post;
import com.hh.mirishop.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long createComment(CommentRequest request, Long memberNumber, Long postId) {
        Post post = findPostById(postId);
        Member member = memberRepository.findById(memberNumber)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));
        Long parentCommentId = request.getParentCommentId();
        if (parentCommentId != null) {
            Comment parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new CommentException(ErrorCode.PARENT_COMMENT_NOT_FOUND));

            if (parentComment.getParentComment() != null) {
                throw new CommentException(ErrorCode.SUBCOMMENT_NOT_ALLOWED);
            }
        }
        Comment comment = Comment.builder()
                .post(post)
                .content(request.getContent())
                .member(member)
                .isDeleted(false)
                .build();

        commentRepository.save(comment);
        /*
        뉴스피드에 대한 로직 고려
        */
        return comment.getCommentId();
    }

    @Override
    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostException(ErrorCode.POST_NOT_FOUND));
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long currentMemberNumber) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(ErrorCode.COMMENT_NOT_FOUND));

        checkAuthorizedMember(currentMemberNumber, comment);

        comment.delete(true);
        commentRepository.save(comment);
    }

    private void checkAuthorizedMember(Long currentMemberNumber, Comment comment) {
        if (!comment.getMember().getNumber().equals(currentMemberNumber)) {
            throw new CommentException(ErrorCode.UNAUTHORIZED_COMMENT_ACCESS);
        }
    }
}
