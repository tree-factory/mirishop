package com.hh.mirishop.user.controller;


import com.hh.mirishop.user.dto.UserRequest;
import com.hh.mirishop.user.service.UserService;
import jakarta.validation.Valid;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid final UserRequest userRequest) {
        final Long userId = userService.register(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Collections.singletonMap("userId", userId));
    }

}
