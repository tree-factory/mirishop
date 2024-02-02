package com.hh.mirishop.auth.service;

import com.hh.mirishop.auth.dto.LoginRequest;
import com.hh.mirishop.auth.dto.TokenResponse;
import com.hh.mirishop.auth.infrastructure.JwtTokenProvider;
import com.hh.mirishop.exception.ErrorCode;
import com.hh.mirishop.exception.MemberException;
import com.hh.mirishop.member.domain.Member;
import com.hh.mirishop.member.repository.MemberRepository;
import com.hh.mirishop.redis.service.RedisService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class LoginService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RedisService redisService;

    @Transactional
    public TokenResponse login(final LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(ErrorCode.MEMBER_NOT_FOUND,
                        String.format("%s는 가입 이력이 없습니다.", email)));

        validatePassword(password, member);
        TokenResponse tokenResponse = getNewTokenResponse(email);

        return tokenResponse;
    }

    private TokenResponse getNewTokenResponse(String email) {
        TokenResponse newTokenResponse = jwtTokenProvider.generateTokenResponse(email);
        redisService.setDataExpire(email, newTokenResponse.getRefreshToken(), 3 * 24 * 60 * 60L);
        return newTokenResponse;
    }

    private void validatePassword(String password, Member member) {
        if (!bCryptPasswordEncoder.matches(password, member.getPassword())) {
            throw new MemberException(ErrorCode.INVALID_PASSWORD, String.format("이메일 또는 패스워드가 잘못 되었습니다."));
        }
    }

    @Transactional
    public TokenResponse reissue(final String refreshToken) {
        // 리프레시 토큰 유효성 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new MemberException(ErrorCode.INVALID_REFRESH_TOKEN, "유효하지 않은 리프레시 토큰입니다.");
        }

        // 리프레시 토큰으로부터 사용자 정보 추출
        String email = jwtTokenProvider.extractEmailFromToken(refreshToken);

        // Redis에 저장된 리프레시 토큰과 비교
        String storedRefreshToken = redisService.getData(email);
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new MemberException(ErrorCode.INVALID_REFRESH_TOKEN, "리프레시 토큰이 불일치 합니다.");
        }

        TokenResponse newTokenResponse = getNewTokenResponse(email);

        return newTokenResponse;
    }


}