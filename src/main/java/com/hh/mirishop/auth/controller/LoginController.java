package com.hh.mirishop.auth.controller;


import com.hh.mirishop.auth.service.LoginService;
import com.hh.mirishop.member.dto.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        String token = loginService.login(loginRequest.getEmail(), loginRequest.getPassword());
        Map<String, String> tokenResponse = Collections.singletonMap("token", token);
        return ResponseEntity.ok(tokenResponse);
    }

}
