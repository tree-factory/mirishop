package com.hh.mirishop.user.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateRequest {

    private String nickName;
    private String profileImage;
    private String bio;
}
