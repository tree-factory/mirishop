package com.hh.mirishop.user.controller;


import com.hh.mirishop.email.repository.EmailRequest;
import com.hh.mirishop.email.service.EmailService;
import com.hh.mirishop.user.dto.UserRequest;
import com.hh.mirishop.user.service.UserService;
import jakarta.validation.Valid;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid final UserRequest userRequest) {
        final Long userId = userService.register(userRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap("userId", userId));
    }

     /*
    이메일 인증요청
     */
    @PostMapping("/email-authentication")
    public ResponseEntity<Void> requestEmailVerification(@RequestBody @Valid EmailRequest request) {
        emailService.authEmail(request);
        return ResponseEntity.ok().build();
    }

    /*
    인증코드 검증
    */
    @GetMapping("/email-verification")
    public ResponseEntity<String> verifyEmail(@RequestParam("email") String email,
                                              @RequestParam("verificationCode") String verificationCode) {
        System.out.println(email);
        System.out.println(verificationCode);
        boolean isVerified = emailService.verityEmail(email, verificationCode);
        if (isVerified) {
            return ResponseEntity.ok("이메일 인증 성공");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일 인증 실패");
    }


}
