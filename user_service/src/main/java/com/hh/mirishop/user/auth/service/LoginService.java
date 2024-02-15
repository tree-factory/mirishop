package com.hh.mirishop.user.auth.service;

import com.hh.mirishop.user.auth.dto.LoginRequest;
import com.hh.mirishop.user.auth.dto.TokenResponse;

public interface LoginService {

    TokenResponse login(final LoginRequest loginRequest);

    TokenResponse reissue(final String refreshToken);
}