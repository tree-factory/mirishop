package com.hh.mirishop.activity.like.controller;

import com.hh.mirishop.activity.common.dto.BaseResponse;
import com.hh.mirishop.activity.like.service.LikeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {

    private final LikeServiceImpl likeService;

    @PostMapping("/posts/{postId}")
    public ResponseEntity<BaseResponse<Void>> likePost(@PathVariable("postId") Long postId,
                                                       @RequestParam("member") Long currentMemberNumber) {
        likeService.likePost(postId, currentMemberNumber);
        return ResponseEntity.ok(BaseResponse.of("글 좋아요가 완료 되었습니다.", true, null));
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<BaseResponse<Void>> unlikePost(@PathVariable("postId") Long postId,
                                                         @RequestParam("member") Long currentMemberNumber) {
        likeService.unlikePost(postId, currentMemberNumber);
        return ResponseEntity.ok(BaseResponse.of("글 좋아요 취소가 완료 되었습니다.", true, null));
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse<Void>> likeComment(@PathVariable("commentId") Long commentId,
                                                          @RequestParam("member") Long currentMemberNumber) {
        likeService.likeComment(commentId, currentMemberNumber);
        return ResponseEntity.ok(BaseResponse.of("댓글 좋아요가 완료 되었습니다.", true, null));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponse<Void>> unlikeComment(@PathVariable("commentId") Long commentId,
                                                            @RequestParam("member") Long currentMemberNumber) {
        likeService.unlikeComment(commentId, currentMemberNumber);
        return ResponseEntity.ok(BaseResponse.of("댓글 좋아요 취소가 완료 되었습니다.", true, null));
    }
}
