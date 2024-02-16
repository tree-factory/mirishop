package com.hh.mirishop.activity.post.controller;

import com.hh.mirishop.activity.common.dto.BaseResponse;
import com.hh.mirishop.activity.post.dto.PostRequest;
import com.hh.mirishop.activity.post.dto.PostResponse;
import com.hh.mirishop.activity.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /*
    게시글 작성
    */
    @PostMapping
    public ResponseEntity<BaseResponse<Void>> createPost(@Valid @RequestBody PostRequest postRequest,
                                                         @RequestParam(name = "member") Long currentMemberNumber) {
        Long postId = postService.createPost(postRequest, currentMemberNumber);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{postId}")
                .buildAndExpand(postId)
                .toUri();

        return ResponseEntity.created(location).body(BaseResponse.of("게시글이 생성되었습니다.", true, null));
    }

    /*
    본인이 작성한 게시글 보는 메소드
    */
    @GetMapping
    public ResponseEntity<BaseResponse<Page<PostResponse>>> getAllPosts(@RequestParam("page") int page,
                                                                        @RequestParam("size") int size,
                                                                        @RequestParam(name = "member") Long currentMemberNumber) {
        Page<PostResponse> postList = postService.getAllpostsByMember(page - 1, size, currentMemberNumber);

        return ResponseEntity.ok(BaseResponse.of("게시글 목록 조회 성공", true, postList));
    }

    /*
    게시글 한개 보기
    */
    @GetMapping("/{postId}")
    public ResponseEntity<BaseResponse<PostResponse>> getPost(@PathVariable Long postId){
        PostResponse postResponse = postService.getPost(postId);
        return ResponseEntity.ok(BaseResponse.of("게시글 내용 조회 성공", true, postResponse));
    }

    /*
    Todo : 비회원이 게시글 모음을 볼지, 뉴스피드를 볼지 고민해야함.
    */

    /*
    게시글 수정
    */
    @PatchMapping("/{postId}")
    public ResponseEntity<BaseResponse<Void>> updatePost(@PathVariable("postId") Long postId,
                                                         @RequestBody PostRequest postRequest,
                                                         @RequestParam(name = "member") Long currentMemberNumber) {
        postService.updatePost(postId, postRequest, currentMemberNumber);
        return ResponseEntity.ok(BaseResponse.of("게시글이 업데이트되었습니다.", true, null));
    }

    /*
    게시글 삭제
    */
    @DeleteMapping("/{postId}")
    public ResponseEntity<BaseResponse<Void>> deletePost(@PathVariable("postId") Long postId,
                                                         @RequestParam(name = "member") Long currentMemberNumber) {
        postService.deletePost(postId, currentMemberNumber);
        return ResponseEntity.ok(BaseResponse.of("게시글이 삭제되었습니다.", true, null));
    }
}
