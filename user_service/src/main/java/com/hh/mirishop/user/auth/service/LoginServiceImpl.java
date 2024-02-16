package com.hh.mirishop.user.auth.service;

import com.hh.mirishop.user.auth.infrastructure.JwtTokenProvider;
import com.hh.mirishop.user.auth.dto.LoginRequest;
import com.hh.mirishop.user.auth.dto.TokenResponse;
import com.hh.mirishop.user.common.exception.ErrorCode;
import com.hh.mirishop.user.common.exception.JwtTokenException;
import com.hh.mirishop.user.common.exception.MemberException;
import com.hh.mirishop.user.common.redis.service.AuthRedisService;
import com.hh.mirishop.user.member.entity.Member;
import com.hh.mirishop.user.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final AuthRedisService authRedisService;

    @Override
    @Transactional
    public TokenResponse login(final LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        validatePassword(password, member);

        return getNewTokenResponse(member.getNumber(), member.getEmail());
    }

    @Override
    @Transactional
    public TokenResponse reissue(final String refreshToken) {
        // 리프레시 토큰 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new JwtTokenException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 리프레시 토큰으로부터 사용자 정보 추출
        String email = jwtTokenProvider.extractEmailFromRefreshToken(refreshToken);

        // Redis에 저장된 리프레시 토큰과 비교
        String storedRefreshToken = authRedisService.getData(email);
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new JwtTokenException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND));

        return getNewTokenResponse(member.getNumber(), email);
    }

    /*
    accessToken, refreshToken을 모두 재발급하여, 기존 refreshToken 사용을 만료시킴
    */
    private TokenResponse getNewTokenResponse(Long memberNumber, String email) {
        TokenResponse newTokenResponse = jwtTokenProvider.generateTokenResponse(memberNumber, email);
        authRedisService.setDataExpire(email, newTokenResponse.getRefreshToken(), 3 * 24 * 60 * 60L);
        return newTokenResponse;
    }

    private void validatePassword(String password, Member member) {
        if (!bCryptPasswordEncoder.matches(password, member.getPassword())) {
            throw new MemberException(ErrorCode.INVALID_PASSWORD);
        }
    }
}