package com.hh.mirishop.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
}