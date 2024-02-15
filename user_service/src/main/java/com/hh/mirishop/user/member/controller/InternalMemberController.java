package com.hh.mirishop.user.member.controller;

import com.hh.mirishop.user.member.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/internal/members")
@RequiredArgsConstructor
public class InternalMemberController {

    private final MemberQueryService memberQueryService;

    @GetMapping("/{memberNumber}")
    public boolean existsMemberByNumber(@PathVariable("memberNumber") Long memberNumber) {
        return memberQueryService.existsMemberByNumber(memberNumber);
    }
}
