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
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class LoginService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
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

        TokenResponse tokenResponse = jwtTokenProvider.generateTokenResponse(email);

        redisService.setDataExpire(email, tokenResponse.getRefreshToken(), 3 * 24 * 60 * 60L); ;

        return tokenResponse;
    }

    private void validatePassword(String password, Member member) {
        if (!bCryptPasswordEncoder.matches(password, member.getPassword())) {
            throw new MemberException(ErrorCode.INVALID_PASSWORD, String.format("이메일 또는 패스워드가 잘못 되었습니다."));
        }
    }

//    @Transactional
//    public TokenResponseDto reissue(final TokenRequestDto tokenRequestDto) {
//        validateRefreshToken(tokenRequestDto);
//
//        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
//        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
//                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
//
//        validateRefreshTokenOwner(refreshToken, tokenRequestDto);
//
//        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
//        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
//        refreshTokenRepository.save(newRefreshToken);
//
//        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
//    }
}