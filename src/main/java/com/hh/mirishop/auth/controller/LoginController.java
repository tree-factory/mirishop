package com.hh.mirishop.auth.controller;

import com.hh.mirishop.auth.dto.LoginRequest;
import com.hh.mirishop.auth.dto.TokenRequest;
import com.hh.mirishop.auth.dto.TokenResponse;
import com.hh.mirishop.auth.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    /*
    로그인
    */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        TokenResponse token = loginService.login(loginRequest);
        return ResponseEntity.ok(token);
    }

    /*
    토큰 재발급
    */
    @PostMapping("/refreshToken")
    public ResponseEntity<TokenResponse> reissueToken(@RequestBody TokenRequest tokenRequest) {
        TokenResponse token = loginService.reissue(tokenRequest.getRefreshToken());
        return ResponseEntity.ok(token);
    }
}
