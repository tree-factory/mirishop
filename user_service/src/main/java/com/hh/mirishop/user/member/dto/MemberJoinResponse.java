package com.hh.mirishop.user.member.dto;

import com.hh.mirishop.user.member.entity.Member;
import lombok.Getter;

@Getter
public class MemberJoinResponse {

    private final Long number;
    private final String email;

    public MemberJoinResponse(Member member) {
        this.number = member.getNumber();
        this.email = member.getEmail();
    }
}
