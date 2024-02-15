package com.hh.newsfeed.newsfeed.controller;

import com.hh.newsfeed.auth.domain.UserDetailsImpl;
import com.hh.newsfeed.common.dto.BaseResponse;
import com.hh.newsfeed.newsfeed.dto.ActivityResponse;
import com.hh.newsfeed.newsfeed.service.NewsFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/newsfeed")
@RequiredArgsConstructor
public class NewsFeedController {

    private final NewsFeedService newsFeedService;

    @GetMapping("/my")
    public ResponseEntity<BaseResponse<Page<ActivityResponse>>> getMyNewsFeed(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Long currentMemberNumber = userDetails.getNumber();

        Page<ActivityResponse> newsFeed = newsFeedService.getNewsfeedForMember(page - 1, size, userDetails);

        return new ResponseEntity<>(BaseResponse.of("뉴스피드 조회 성공", true, newsFeed), HttpStatus.OK);
    }
}
