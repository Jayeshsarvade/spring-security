package com.springsecurity.Spring_security.controller;

import com.springsecurity.Spring_security.dto.*;
import com.springsecurity.Spring_security.entity.User;
import com.springsecurity.Spring_security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * This method handles the sign-up process for a new user.
     *
     * @param signUpRequest The request object containing the user's sign up details.
     * @return A ResponseEntity with HTTP status 201 (Created) and the created User object.
     *
     * @throws IllegalArgumentException If the sign-up request is invalid.
     * @throws Exception If any other error occurs during the sign-up process.
     */

    @Operation(summary = "SignUp User")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User Created",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SignUpRequest.class)) })})
    @PostMapping("/signUp")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authenticationService.signUp(signUpRequest));
    }

    /**
     * This method handles the sign-in process for an existing user.
     *
     * @param signInRequest The request object containing the user's sign in details.
     * @return A ResponseEntity with HTTP status 200 (OK) and the JwtAuthenticationResponse object.
     *
     * @throws IllegalArgumentException If the sign in request is invalid.
     * @throws Exception If any other error occurs during the sign-in process.
     */
    @Operation(summary = "SignIn User")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User Created",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SignInRequest.class)) })})
    @PostMapping("/signIn")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@Valid @RequestBody SignInRequest signInRequest){
        return ResponseEntity.ok(authenticationService.signIn(signInRequest));
    }

    /**
     * This method handles the refresh token process for an authenticated user.
     *
     * @param refreshTokenRequest The request object containing the user's refresh token.
     * @return A ResponseEntity with HTTP status 200 (OK) and the JwtAuthenticationResponse object.
     *
     * @throws IllegalArgumentException If the refresh token request is invalid.
     * @throws Exception If any other error occurs during the refresh token process.
     */
    @Operation(summary = "RefreshToken User")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User Created",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RefreshTokenRequest.class)) })})
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }
}
