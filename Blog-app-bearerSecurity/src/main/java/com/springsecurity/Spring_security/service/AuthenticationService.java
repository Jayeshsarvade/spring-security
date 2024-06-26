package com.springsecurity.Spring_security.service;

import com.springsecurity.Spring_security.dto.*;
import com.springsecurity.Spring_security.entity.User;

public interface AuthenticationService {

    UserDto signUp(SignUpRequest signUpRequest);
    JwtAuthenticationResponse signIn(SignInRequest signInRequest);
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
