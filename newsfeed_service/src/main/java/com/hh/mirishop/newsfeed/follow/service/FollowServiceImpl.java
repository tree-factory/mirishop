package com.hh.newsfeed.follow.service;

import com.hh.newsfeed.common.exception.ErrorCode;
import com.hh.newsfeed.common.exception.FollowException;
import com.hh.newsfeed.common.exception.MemberException;
import com.hh.newsfeed.follow.domain.FollowId;
import com.hh.newsfeed.follow.dto.FollowRequest;
import com.hh.newsfeed.follow.entity.Follow;
import com.hh.newsfeed.follow.repository.FollowRepository;
import com.hh.newsfeed.member.entity.Member;
import com.hh.newsfeed.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowServiceImpl implements FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    /*
    팔로우
    */
    @Override
    @Transactional
    public void follow(FollowRequest followRequest, Long currentMemberNumber) {
        Long followMemberNumber = followRequest.getFollowingMemberNumber();
        // 자기 자신 팔로우인지 확인
        validateFollowSelf(followMemberNumber, currentMemberNumber);

        // 멤버 DB 검증
        Member currentMember = memberRepository.findById(currentMemberNumber)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        Member memberToFollow = memberRepository.findById(followMemberNumber)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        // 팔로우 가능한 멤버인지 검증
        FollowId followId = new FollowId(currentMemberNumber, followMemberNumber);
        followRepository.findById(followId).ifPresent(f -> {
            throw new FollowException(ErrorCode.DUPLICATE_FOLLOW);
        });

        Follow follow = new Follow(currentMember, memberToFollow);
        followRepository.save(follow);
    }

    @Override
    @Transactional
    public void unfollow(FollowRequest followRequest, Long currentMemberNumber) {
        Long unfollowUserNumber = followRequest.getFollowingMemberNumber();
        FollowId followId = new FollowId(currentMemberNumber, unfollowUserNumber);

        Follow follow = followRepository.findById(followId)
                .orElseThrow(() -> new FollowException(ErrorCode.FOLLOW_NOT_FOUND));

        followRepository.delete(follow);
    }

    private void validateFollowSelf(Long currentMemberNumber, Long followMemberNumber) {
        if (currentMemberNumber.equals(followMemberNumber)) {
            throw new FollowException(ErrorCode.CANNOT_FOLLOW_SELF);
        }
    }
}
