package com.hh.mirishop.auth.service;

import com.hh.mirishop.auth.dto.LoginRequest;
import com.hh.mirishop.auth.dto.TokenResponse;
import com.hh.mirishop.auth.infrastructure.JwtTokenProvider;
import com.hh.mirishop.common.exception.ErrorCode;
import com.hh.mirishop.common.exception.MemberException;
import com.hh.mirishop.common.redis.service.AuthRedisService;
import com.hh.mirishop.member.entity.Member;
import com.hh.mirishop.member.repository.MemberRepository;
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
        TokenResponse tokenResponse = getNewTokenResponse(email);

        return tokenResponse;
    }

    @Override
    @Transactional
    public TokenResponse reissue(final String refreshToken) {
        // 리프레시 토큰 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new MemberException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 리프레시 토큰으로부터 사용자 정보 추출
        String email = jwtTokenProvider.extractEmailFromRefreshToken(refreshToken);

        // Redis에 저장된 리프레시 토큰과 비교
        String storedRefreshToken = authRedisService.getData(email);
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new MemberException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        TokenResponse newTokenResponse = getNewTokenResponse(email);

        return newTokenResponse;
    }

    private TokenResponse getNewTokenResponse(String email) {
        TokenResponse newTokenResponse = jwtTokenProvider.generateTokenResponse(email);
        authRedisService.setDataExpire(email, newTokenResponse.getRefreshToken(), 3 * 24 * 60 * 60L);
        return newTokenResponse;
    }

    private void validatePassword(String password, Member member) {
        if (!bCryptPasswordEncoder.matches(password, member.getPassword())) {
            throw new MemberException(ErrorCode.INVALID_PASSWORD);
        }
    }
}