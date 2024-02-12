package com.hh.mirishop.auth.service;

import com.hh.mirishop.auth.dto.LoginRequest;
import com.hh.mirishop.auth.dto.TokenResponse;

public interface LoginService {

    TokenResponse login(final LoginRequest loginRequest);

    TokenResponse reissue(final String refreshToken);
}