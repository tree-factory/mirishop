package com.hh.mirishop.user.member.controller;


import com.hh.mirishop.user.auth.domain.UserDetailsImpl;
import com.hh.mirishop.user.common.dto.BaseResponse;
import com.hh.mirishop.user.email.repository.EmailRequest;
import com.hh.mirishop.user.email.service.EmailService;
import com.hh.mirishop.user.member.dto.ChangePasswordRequest;
import com.hh.mirishop.user.member.dto.MemberDetailResponse;
import com.hh.mirishop.user.member.dto.MemberJoinResponse;
import com.hh.mirishop.user.member.dto.MemberRequest;
import com.hh.mirishop.user.member.dto.MemberUpdateRequest;
import com.hh.mirishop.user.member.service.ImageUploadService;
import com.hh.mirishop.user.member.service.MemberQueryService;
import com.hh.mirishop.user.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MemberQueryService memberQueryService;
    private final EmailService emailService;
    private final ImageUploadService imageUploadService;

    /*
    회원가입
     */
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<MemberJoinResponse>> register(
            @RequestBody @Valid final MemberRequest memberRequest) {
        MemberJoinResponse joinMember = memberService.register(memberRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.of("회원가입 성공", true, joinMember));
    }

    /*
    이메일 인증요청
    */
    @PostMapping("/email-authentication")
    public ResponseEntity<BaseResponse<Void>> requestEmailVerification(@RequestBody @Valid EmailRequest request) {
        emailService.authEmail(request);
        return ResponseEntity.ok(BaseResponse.of("이메일 인증 요청 성공", true, null));
    }

    /*
    인증코드 검증
    */
    @GetMapping("/email-verification")
    public ResponseEntity<BaseResponse<String>> verifyEmail(@RequestParam("email") String email,
                                                            @RequestParam("verificationCode") String verificationCode) {
        boolean isVerified = emailService.verityEmail(email, verificationCode);
        if (isVerified) {
            return ResponseEntity.ok(BaseResponse.of("이메일 인증 성공", true, "이메일 인증 성공"));
        }
        return ResponseEntity.badRequest().body(BaseResponse.of("이메일 인증 실패", false, "이메일 인증 실패"));
    }

    // 프론트단에 저장된 업로드 경로를 리턴
    @PostMapping("/upload/image")
    public ResponseEntity<BaseResponse<String>> uploadProfileImage(@RequestParam("image") MultipartFile imageFile) {
        log.info("엔드포인트 접속확인");
        try {
            String imagePath = imageUploadService.uploadImage(imageFile);
            return ResponseEntity.ok(BaseResponse.of("이미지 업로드 성공", true, imagePath)); // 저장된 이미지의 경로 반환
        } catch (IllegalArgumentException | IOException e) {
            log.error("이미지 업로드 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(BaseResponse.of("이미지 업로드에 실패했습니다.", false, null));
        }
    }

    /*
    유저 정보 조회
    */
    @GetMapping("/{memberNumber}")
    public ResponseEntity<BaseResponse<MemberDetailResponse>> getMemberDetail(
            @PathVariable("memberNumber") Long memberNumber) {
        MemberDetailResponse memberDetail = memberQueryService.getMemberDetail(memberNumber);
        return ResponseEntity.ok(BaseResponse.of("유저 정보를 불러왔습니다.", true, memberDetail));
    }

    /*
    유저 정보 수정
    */
    @PatchMapping
    public ResponseEntity<BaseResponse<Void>> update(
            @Valid @RequestBody MemberUpdateRequest memberUpdateRequest, @AuthenticationPrincipal
    UserDetailsImpl userDetails) {
        memberService.update(memberUpdateRequest, userDetails);
        return ResponseEntity.ok().body(BaseResponse.of("유저 정보 변경 완료", true, null));
    }

    /*
    비밀번호 정보 수정
    */
    @PutMapping("/password")
    public ResponseEntity<BaseResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest, @AuthenticationPrincipal
    UserDetailsImpl userDetails) {
        memberService.changePassword(changePasswordRequest, userDetails);
        return ResponseEntity.ok().body(BaseResponse.of("패스워드 변경 완료", true, null));
    }

}
