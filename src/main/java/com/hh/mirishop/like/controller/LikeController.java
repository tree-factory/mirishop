package com.hh.mirishop.like.controller;

import com.hh.mirishop.auth.domain.UserDetailsImpl;
import com.hh.mirishop.common.dto.BaseResponse;
import com.hh.mirishop.like.service.LikeService;
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
@RequestMapping("/api/v1/posts/{postId}")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/likes")
    public ResponseEntity<BaseResponse<Void>> likePost(@PathVariable("postId") Long postId,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.likePost(postId, userDetails.getNumber());
        return ResponseEntity.ok(BaseResponse.of("좋아요가 완료 되었습니다.", true, null));
    }


    @DeleteMapping("/likes")
    public ResponseEntity<BaseResponse<Void>> unlikePost(@PathVariable("postId") Long postId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.unlikePost(postId, userDetails.getNumber());
        return ResponseEntity.ok(BaseResponse.of("좋아요 취소가 완료 되었습니다.", true, null));
    }
}
