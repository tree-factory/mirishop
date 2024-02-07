package com.hh.mirishop.comment.controller;

import com.hh.mirishop.auth.domain.UserDetailsImpl;
import com.hh.mirishop.comment.dto.CommentRequest;
import com.hh.mirishop.comment.service.CommentService;
import com.hh.mirishop.common.dto.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> createComment(@Valid @RequestBody CommentRequest commentRequest,
                                                            @PathVariable("postId") Long postId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long commentId = commentService.createComment(commentRequest, userDetails.getNumber(), postId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{commentId}")
                .buildAndExpand(commentId)
                .toUri();

        return ResponseEntity.created(location).body(BaseResponse.of("댓글이 생성되었습니다.", true, null));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<BaseResponse<Void>> deleteComment(@PathVariable("commentId") Long commentId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId, userDetails.getNumber());
        return ResponseEntity.ok(BaseResponse.of("댓글이 삭제되었습니다.", true, null));
    }
}
