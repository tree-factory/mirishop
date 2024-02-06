package com.hh.mirishop.follow.controller;

import com.hh.mirishop.auth.domain.UserDetailsImpl;
import com.hh.mirishop.common.dto.BaseResponse;
import com.hh.mirishop.follow.dto.FollowRequest;
import com.hh.mirishop.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follows")
public class FollowController {

    private final FollowService followService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> follow(@RequestBody final FollowRequest followRequest,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        followService.follow(followRequest, userDetails.getNumber());

        return new ResponseEntity<>(BaseResponse.of("팔로우 추가", true, null),
                HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<BaseResponse<Void>> unfollow(@RequestBody final FollowRequest followRequest,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long currentMemberNumber = userDetails.getNumber();
        followService.unfollow(followRequest, currentMemberNumber);

        return new ResponseEntity<>(BaseResponse.of("언팔로우", true, null), HttpStatus.OK);
    }
}
