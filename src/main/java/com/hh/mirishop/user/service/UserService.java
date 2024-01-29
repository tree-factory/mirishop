package com.hh.mirishop.user.service;

import static com.hh.mirishop.common.Constants.USER_PASSWORD_LENGTH;
import static com.hh.mirishop.user.exception.UserExceptionMessage.DUPLICATED_EMAIL;
import static com.hh.mirishop.user.exception.UserExceptionMessage.INVALID_EMAIL_FROM;
import static com.hh.mirishop.user.exception.UserExceptionMessage.INVALID_PASSWORD_LENGTH;

import com.hh.mirishop.user.domain.User;
import com.hh.mirishop.user.dto.UserRequest;
import com.hh.mirishop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    // 기본 이미지 경로는 추후 업로드 방식이 변경되면 수정 필요
    private static final String DEFAULT_PROFILE_IMAGE_PATH = "/uploads/images/default.jpg";

    private final static Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Long register(final UserRequest userRequest) {
        String email = userRequest.getEmail();
        String password = userRequest.getPassword();
        String profileImagePath = userRequest.getProfileImage();

        validateEmail(email);
        validatePassword(password);
        validateUploadProfileImage(profileImagePath);

        String encodedPassword = encodePassword(password);

        final User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(encodedPassword)
                .profileImage(userRequest.getProfileImage())
                .bio(userRequest.getBio())
                .build();

        final User userEntity = userRepository.save(user);

        return userEntity.getId();
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
        userRepository.findByEmail(email)
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
