package com.hh.mirishop.user.email.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailRequest {
    @Email
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;
}