package com.hh.mirishop.user.member.service;

import com.hh.mirishop.user.auth.domain.UserDetailsImpl;
import com.hh.mirishop.user.member.dto.ChangePasswordRequest;
import com.hh.mirishop.user.member.dto.MemberJoinResponse;
import com.hh.mirishop.user.member.dto.MemberRequest;
import com.hh.mirishop.user.member.dto.MemberUpdateRequest;

public interface MemberService {

    MemberJoinResponse register(final MemberRequest memberRequest);

    void update(MemberUpdateRequest memberUpdateRequest, UserDetailsImpl userDetails);

    void changePassword(ChangePasswordRequest changePasswordRequest, UserDetailsImpl userDetails);
}
