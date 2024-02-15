package com.hh.newsfeed.auth.service;

import com.hh.newsfeed.auth.dto.LoginRequest;
import com.hh.newsfeed.auth.dto.TokenResponse;

public interface LoginService {

    TokenResponse login(final LoginRequest loginRequest);

    TokenResponse reissue(final String refreshToken);
}