package com.springsecurity.Spring_security.service.impl;

import com.springsecurity.Spring_security.dto.JwtAuthenticationResponse;
import com.springsecurity.Spring_security.dto.RefreshTokenRequest;
import com.springsecurity.Spring_security.dto.SignInRequest;
import com.springsecurity.Spring_security.dto.SignUpRequest;
import com.springsecurity.Spring_security.entity.Role;
import com.springsecurity.Spring_security.entity.User;
import com.springsecurity.Spring_security.repository.UserRepository;
import com.springsecurity.Spring_security.service.AuthenticationService;
import com.springsecurity.Spring_security.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * This method is used to create a new user.
     *
     * @param signUpRequest The user data to be created. This data should be validated and sanitized before being passed to this method.
     * @return A ResponseEntity containing the created user data and a HTTP status code of 201 (Created).
     * @throws IllegalArgumentException If the userDto is null or invalid.
     */
    public User signUp(SignUpRequest signUpRequest) {
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setContact(signUpRequest.getContact());
        user.setAbout(signUpRequest.getAbout());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        return userRepository.save(user);
    }

    /**
     * This method handles the user sign-in process.
     *
     * @param signInRequest The DTO object containing the user's email and password.
     * @return A JwtAuthenticationResponse object containing the generated JWT and refresh token.
     * @throws IllegalArgumentException If the provided email or password is invalid.
     */
    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail()
                , signInRequest.getPassword()));

        var user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("Invalid Email and password..."));

        var jwt = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }

    /**
     * This method handles the refresh token process.
     *
     * @param refreshTokenRequest The DTO object containing the refresh token.
     * @return A JwtAuthenticationResponse object containing the newly generated JWT and the same refresh token.
     *         If the refresh token is invalid or expired, it returns null.
     * @throws IllegalArgumentException If the provided refresh token is invalid or the user does not exist.
     */

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtService.generateToken(user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();

            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }
        return null;
    }
}
