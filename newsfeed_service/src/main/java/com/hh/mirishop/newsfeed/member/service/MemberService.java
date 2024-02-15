package com.hh.newsfeed.member.service;

import com.hh.newsfeed.auth.domain.UserDetailsImpl;
import com.hh.newsfeed.member.dto.ChangePasswordRequest;
import com.hh.newsfeed.member.dto.MemberRequest;
import com.hh.newsfeed.member.dto.MemberUpdateRequest;

public interface MemberService {

    Long register(final MemberRequest memberRequest);

    void update(MemberUpdateRequest memberUpdateRequest, UserDetailsImpl userDetails);

    void changePassword(ChangePasswordRequest changePasswordRequest, UserDetailsImpl userDetails);
}
