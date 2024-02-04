package com.hh.mirishop.member.service;

import static com.hh.mirishop.common.constants.UserConstants.EMAIL_REGEX;
import static com.hh.mirishop.common.constants.UserConstants.USER_PASSWORD_LENGTH;
import static com.hh.mirishop.common.exception.ErrorCode.DUPLICATED_EMAIL;
import static com.hh.mirishop.common.exception.ErrorCode.INVALID_EMAIL_FROM;
import static com.hh.mirishop.common.exception.ErrorCode.INVALID_PASSWORD_LENGTH;

import com.hh.mirishop.auth.domain.UserDetailsImpl;
import com.hh.mirishop.member.domain.Role;
import com.hh.mirishop.member.dto.ChangePasswordRequest;
import com.hh.mirishop.member.dto.MemberRequest;
import com.hh.mirishop.member.dto.MemberUpdateRequest;
import com.hh.mirishop.member.entity.Member;
import com.hh.mirishop.common.exception.user.SamePasswordException;
import com.hh.mirishop.common.exception.user.WrongPasswordException;
import com.hh.mirishop.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    // 기본 이미지 경로는 추후 업로드 방식이 변경되면 수정 필요
    private static final String DEFAULT_PROFILE_IMAGE_PATH = "/uploads/images/default.jpg";
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

    private void validateUploadProfileImage(String profileImagePath) {
        if (profileImagePath == null || profileImagePath.isEmpty()) {
            profileImagePath = DEFAULT_PROFILE_IMAGE_PATH;
        }
    }

    @Transactional
    public void update(MemberUpdateRequest memberUpdateRequest, UserDetailsImpl userDetails) {
        Member member = memberRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new IllegalArgumentException());// 수정 필요

        String nickname = memberUpdateRequest.getNickName();
        String profileImagePath = memberUpdateRequest.getProfileImage();
        String bio = memberUpdateRequest.getBio();

        validateUploadProfileImage(profileImagePath);

        member.updateNickname(nickname);
        member.updateProfileImage(profileImagePath);
        member.updateBio(bio);

        memberRepository.save(member);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordRequest, UserDetailsImpl userDetails) {
        Member member = memberRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new IllegalArgumentException());// 수정 필요
        String storedPassword = member.getPassword();
        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();
        validatePassword(newPassword);

        // 기존 비밀번호 검증 로직
        if (!isMatchesPassword(oldPassword, storedPassword)) {
            throw new WrongPasswordException();
        }

        // 새로운 비밀번호가 기존 비밀번호와 같은 경우
        if (isMatchesPassword(newPassword, storedPassword)) {
            throw new SamePasswordException();
        }

        member.updatePassword(encodePassword(newPassword));
        memberRepository.save(member);
    }

    private boolean isMatchesPassword(String password, String storedPassword) {
        return bCryptPasswordEncoder.matches(password, storedPassword);
    }
}
