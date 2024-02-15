package com.hh.mirishop.user.auth.service;

import com.hh.mirishop.user.auth.infrastructure.JwtTokenProvider;
import com.hh.mirishop.user.member.entity.Member;
import com.hh.mirishop.user.member.repository.MemberRepository;
import com.hh.mirishop.user.common.exception.ErrorCode;
import com.hh.mirishop.user.common.exception.MemberException;
import com.hh.mirishop.user.common.redis.service.AuthRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final AuthRedisService authRedisService;

    @Override
    @Transactional(readOnly = true)
    public void logout(Long memberNumber) {
        Member member = memberRepository.findByNumberAndIsDeletedFalse(memberNumber)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        authRedisService.deleteData(member.getEmail());
    }
}
