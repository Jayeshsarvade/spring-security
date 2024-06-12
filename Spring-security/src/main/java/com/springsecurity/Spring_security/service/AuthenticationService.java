package com.springsecurity.Spring_security.service;

import com.springsecurity.Spring_security.dto.JwtAuthenticationResponse;
import com.springsecurity.Spring_security.dto.RefreshTokenRequest;
import com.springsecurity.Spring_security.dto.SignInRequest;
import com.springsecurity.Spring_security.dto.SignUpRequest;
import com.springsecurity.Spring_security.entity.User;

public interface AuthenticationService {

    User signUp(SignUpRequest signUpRequest);
    JwtAuthenticationResponse signIn(SignInRequest signInRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
