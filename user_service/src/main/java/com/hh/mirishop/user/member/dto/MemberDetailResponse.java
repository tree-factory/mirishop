package com.hh.mirishop.user.member.dto;

import com.hh.mirishop.user.member.domain.Role;
import com.hh.mirishop.user.member.entity.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberDetailResponse {

    private final Long number;
    private final String email;
    private final String nickname;
    private final String profileImage;
    private final String bio;
    private final Role role;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public MemberDetailResponse(Member member) {
        this.number = member.getNumber();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.profileImage = member.getProfileImage();
        this.bio = member.getBio();
        this.role = member.getRole();
        this.createdAt = member.getCreatedAt();
        this.updatedAt = member.getUpdatedAt();
    }
}
