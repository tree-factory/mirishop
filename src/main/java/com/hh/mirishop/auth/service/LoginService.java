package com.hh.mirishop.auth.service;

import com.hh.mirishop.auth.dto.ProfileTokenResponse;
import com.hh.mirishop.auth.infrastructure.JwtTokenProvider;
import com.hh.mirishop.member.domain.Member;
import com.hh.mirishop.member.exception.ErrorCode;
import com.hh.mirishop.member.exception.MemberException;
import com.hh.mirishop.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public ProfileTokenResponse createToken(Member member) {
        String token = jwtTokenProvider.createToken(member.getEmail());
        return new ProfileTokenResponse(member, token);
    }

    public Long findMemberIdByToken(String token) {
        return jwtTokenProvider.extractId(token);
    }

    public String login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND,
                        String.format("%s는 가입 이력이 없습니다.", email)));

        if (!bCryptPasswordEncoder.matches(password, member.getPassword())) {
            throw new MemberException(ErrorCode.INVALID_PASSWORD, String.format("이메일 또는 패스워드가 잘못 되었습니다."));
        }

        String accessToken = jwtTokenProvider.createToken(email);

        return jwtTokenProvider.createToken(email);
    }
}