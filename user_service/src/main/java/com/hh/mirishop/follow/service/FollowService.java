package com.hh.mirishop.follow.service;

import com.hh.mirishop.follow.dto.FollowRequest;


public interface FollowService {

    void follow(FollowRequest followRequest, Long currentMemberNumber);

    void unfollow(FollowRequest followRequest, Long currentMemberNumber);
}
