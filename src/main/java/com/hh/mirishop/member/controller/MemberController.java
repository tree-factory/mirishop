package com.hh.mirishop.member.controller;


import com.hh.mirishop.email.repository.EmailRequest;
import com.hh.mirishop.email.service.EmailService;
import com.hh.mirishop.member.dto.MemberRequest;
import com.hh.mirishop.member.service.ImageUploadService;
import com.hh.mirishop.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final ImageUploadService imageUploadService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid final MemberRequest memberRequest) {
        final Long userId = memberService.register(memberRequest);

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

    // 프론트단에 저장된 업로드 경로를 리턴
    @PostMapping("/upload/image")
    public ResponseEntity<String> uploadProfileImage(@RequestParam("image") MultipartFile imageFile) {
        try {
            String imagePath = imageUploadService.uploadImage(imageFile);
            return ResponseEntity.ok(imagePath); // 저장된 이미지의 경로 반환
        } catch (IllegalArgumentException e) {
            log.error("잘못된 이미지 업로드 요청", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            log.error("이미지 업로드 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미지 업로드에 실패했습니다.");
        }
    }
}
