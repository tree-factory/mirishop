package com.hh.mirishop.like.controller;

import com.hh.mirishop.auth.domain.UserDetailsImpl;
import com.hh.mirishop.common.dto.BaseResponse;
import com.hh.mirishop.like.service.LikeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeServiceImpl likeService;

    @PostMapping("/posts/{postId}")
    public ResponseEntity<BaseResponse<Void>> likePost(@PathVariable("postId") Long postId,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.likePost(postId, userDetails.getNumber());
        return ResponseEntity.ok(BaseResponse.of("글 좋아요가 완료 되었습니다.", true, null));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<BaseResponse<Void>> unlikePost(@PathVariable("postId") Long postId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.unlikePost(postId, userDetails.getNumber());
        return ResponseEntity.ok(BaseResponse.of("글 좋아요 취소가 완료 되었습니다.", true, null));
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse<Void>> likeComment(@PathVariable("commentId") Long commentId,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.likeComment(commentId, userDetails.getNumber());
        return ResponseEntity.ok(BaseResponse.of("댓글 좋아요가 완료 되었습니다.", true, null));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse<Void>> unlikeComment(@PathVariable("commentId") Long commentId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.unlikeComment(commentId, userDetails.getNumber());
        return ResponseEntity.ok(BaseResponse.of("댓글 좋아요 취소가 완료 되었습니다.", true, null));
    }
}
