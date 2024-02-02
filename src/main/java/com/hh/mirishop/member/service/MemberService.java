package com.hh.mirishop.member.service;

import static com.hh.mirishop.common.Constants.USER_PASSWORD_LENGTH;
import static com.hh.mirishop.exception.ErrorCode.DUPLICATED_EMAIL;
import static com.hh.mirishop.exception.ErrorCode.INVALID_EMAIL_FROM;
import static com.hh.mirishop.exception.ErrorCode.INVALID_PASSWORD_LENGTH;

import com.hh.mirishop.member.domain.Member;
import com.hh.mirishop.member.domain.Role;
import com.hh.mirishop.member.dto.MemberRequest;
import com.hh.mirishop.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MemberService {

    // 기본 이미지 경로는 추후 업로드 방식이 변경되면 수정 필요
    private static final String DEFAULT_PROFILE_IMAGE_PATH = "/uploads/images/default.jpg";
    private final static Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Long register(final MemberRequest memberRequest) {
        String email = memberRequest.getEmail();
        String password = memberRequest.getPassword();
        String profileImagePath = memberRequest.getProfileImage();

        validateEmail(email);
        validatePassword(password);
        validateUploadProfileImage(profileImagePath);

        String encodedPassword = encodePassword(password);

        final Member user = Member.builder()
                .nickname(memberRequest.getName())
                .email(memberRequest.getEmail())
                .password(encodedPassword)
                .profileImage(memberRequest.getProfileImage())
                .bio(memberRequest.getBio())
                .role(Role.ROLE_USER) // role 설정
                .isDeleted(false) // 기본값 false
                .build();

        final Member userEntity = memberRepository.save(user);

        return userEntity.getNumber();
    }

    private void validateEmail(String email) {
        validatedEmailForm(email);
        validatedDuplicatedEmail(email);
    }

    private void validatedEmailForm(String email) {
        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new IllegalArgumentException(INVALID_EMAIL_FROM.getMessage());
        }
    }

    private void validatedDuplicatedEmail(String email) {
        memberRepository.findByEmail(email)
                .ifPresent(existingUser -> {
                    throw new IllegalArgumentException(DUPLICATED_EMAIL.getMessage());
                });
    }

    private void validatePassword(String password) {
        if (password.length() < USER_PASSWORD_LENGTH) {
            throw new IllegalArgumentException(INVALID_PASSWORD_LENGTH.getMessage());
        }
    }

    private String encodePassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

    private static void validateUploadProfileImage(String profileImagePath) {
        if (profileImagePath == null || profileImagePath.isEmpty()) {
            profileImagePath = DEFAULT_PROFILE_IMAGE_PATH;
        }
    }
}
