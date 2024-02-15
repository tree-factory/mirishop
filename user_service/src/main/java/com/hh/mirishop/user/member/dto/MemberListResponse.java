package com.hh.mirishop.user.member.dto;

import com.hh.mirishop.user.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberListResponse {

    private final Long number;
    private final String email;
    private final String nickname;
    private final String profileImage;

    public MemberListResponse(Member member) {
        this.number = member.getNumber();
        this.email = member.getEmail();
        this.nickname = member.getEmail();
        this.profileImage = member.getProfileImage();
    }
}
