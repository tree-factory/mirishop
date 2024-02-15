package com.hh.newsfeed.newsfeed.service;

import com.hh.newsfeed.auth.domain.UserDetailsImpl;
import com.hh.newsfeed.like.entity.Like;
import com.hh.newsfeed.newsfeed.dto.ActivityResponse;
import org.springframework.data.domain.Page;

public interface NewsFeedService {

    Page<ActivityResponse> getNewsfeedForMember(int page, int size, UserDetailsImpl userDetails);

    void createActivityForLike(Like like);

    void deleteActivityForUnlike(Like like);
}
