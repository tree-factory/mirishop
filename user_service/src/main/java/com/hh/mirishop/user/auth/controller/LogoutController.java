package com.hh.mirishop.user.auth.controller;

import com.hh.mirishop.user.auth.domain.UserDetailsImpl;
import com.hh.mirishop.user.auth.service.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LogoutController {

    private final LogoutService logoutService;

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        logoutService.logout(userDetails.getNumber());

        return ResponseEntity.ok().body("로그아웃 되었습니다.");
    }
}
