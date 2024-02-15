package com.hh.newsfeed.follow.service;

import com.hh.newsfeed.follow.dto.FollowRequest;


public interface FollowService {

    void follow(FollowRequest followRequest, Long currentMemberNumber);

    void unfollow(FollowRequest followRequest, Long currentMemberNumber);
}
