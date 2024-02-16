package com.hh.mirishop.newsfeed.service;

import com.hh.mirishop.auth.domain.UserDetailsImpl;
import com.hh.mirishop.like.entity.Like;
import com.hh.mirishop.newsfeed.dto.ActivityResponse;
import org.springframework.data.domain.Page;

public interface NewsFeedService {

    Page<ActivityResponse> getNewsfeedForMember(int page, int size, UserDetailsImpl userDetails);

    void createActivityForLike(Like like);

    void deleteActivityForUnlike(Like like);
}