package com.hh.mirishop.user.service;

import static com.hh.mirishop.user.exception.UserExceptionMessage.DUPLICATED_EMAIL;
import static com.hh.mirishop.user.exception.UserExceptionMessage.INVALID_EMAIL_FROM;

import com.hh.mirishop.email.service.EmailService;
import com.hh.mirishop.redis.service.RedisService;
import com.hh.mirishop.user.domain.User;
import com.hh.mirishop.user.dto.UserRequest;
import com.hh.mirishop.user.repository.UserRepository;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final static Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final RedisService redisService;

    @Transactional
    public Long register(final UserRequest userRequest) {
        validateEmail(userRequest.getEmail());

        final User user = User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
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
}
