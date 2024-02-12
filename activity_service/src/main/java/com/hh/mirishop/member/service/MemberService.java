package com.hh.mirishop.member.service;

import com.hh.mirishop.auth.domain.UserDetailsImpl;
import com.hh.mirishop.member.dto.ChangePasswordRequest;
import com.hh.mirishop.member.dto.MemberRequest;
import com.hh.mirishop.member.dto.MemberUpdateRequest;

public interface MemberService {

    Long register(final MemberRequest memberRequest);

    void update(MemberUpdateRequest memberUpdateRequest, UserDetailsImpl userDetails);

    void changePassword(ChangePasswordRequest changePasswordRequest, UserDetailsImpl userDetails);
}
