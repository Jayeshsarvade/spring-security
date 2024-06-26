package com.springsecurity.Spring_security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.springsecurity.Spring_security.dto.JwtAuthenticationResponse;
import com.springsecurity.Spring_security.dto.RefreshTokenRequest;
import com.springsecurity.Spring_security.dto.SignInRequest;
import com.springsecurity.Spring_security.dto.SignUpRequest;
import com.springsecurity.Spring_security.dto.UserDto;
import com.springsecurity.Spring_security.entity.Role;
import com.springsecurity.Spring_security.entity.User;
import com.springsecurity.Spring_security.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import com.springsecurity.Spring_security.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AuthenticationServiceImpl.class, AuthenticationManager.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AuthenticationServiceImplTest {
    @MockBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationServiceImpl authenticationServiceImpl;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link AuthenticationServiceImpl#signUp(SignUpRequest)}
     */

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setAbout("About");
        user.setComments(new HashSet<>());
        user.setContact(1L);
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setPost(new ArrayList<>());
        user.setRole(Role.USER);
    }

    @Test
    void testSignUp() {
        // Arrange
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);
        SignUpRequest signUpRequest = mock(SignUpRequest.class);
        when(signUpRequest.getAbout()).thenReturn("About");
        when(signUpRequest.getEmail()).thenReturn("jane.doe@example.org");
        when(signUpRequest.getFirstName()).thenReturn("Jane");
        when(signUpRequest.getLastName()).thenReturn("Doe");
        when(signUpRequest.getPassword()).thenReturn("iloveyou");
        when(signUpRequest.getContact()).thenReturn(1L);

        UserDto actualSignUpResult = authenticationServiceImpl.signUp(signUpRequest);

        verify(signUpRequest).getAbout();
        verify(signUpRequest).getContact();
        verify(signUpRequest).getEmail();
        verify(signUpRequest).getFirstName();
        verify(signUpRequest).getLastName();
        verify(signUpRequest).getPassword();
        verify(userRepository).save(isA(User.class));
        verify(passwordEncoder).encode(isA(CharSequence.class));
        assertEquals("About", actualSignUpResult.getAbout());
        assertEquals("Doe", actualSignUpResult.getLastName());
        assertEquals("Jane", actualSignUpResult.getFirstName());
        assertEquals("iloveyou", actualSignUpResult.getPassword());
        assertEquals("jane.doe@example.org", actualSignUpResult.getEmail());
        assertNull(actualSignUpResult.getAddressDto());
        assertEquals(1, actualSignUpResult.getId().intValue());
        assertEquals(1L, actualSignUpResult.getContact());
        assertEquals(Role.USER, actualSignUpResult.getRole());
    }

    /**
     * Method under test: {@link AuthenticationServiceImpl#signUp(SignUpRequest)}
     */
    @Test
    void testSignUp2() {
        // Arrange
        SignUpRequest signUpRequest = mock(SignUpRequest.class);
        when(signUpRequest.getEmail()).thenThrow(new IllegalArgumentException("foo"));

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> authenticationServiceImpl.signUp(signUpRequest));
        verify(signUpRequest).getEmail();
    }

    /**
     * Method under test: {@link AuthenticationServiceImpl#signIn(SignInRequest)}
     */
    @Test
    void testSignIn() throws AuthenticationException {
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));

        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtService.generateRefreshToken(Mockito.<Map<String, Object>>any(), Mockito.<UserDetails>any()))
                .thenReturn("ABC123");
        when(jwtService.generateToken(Mockito.<UserDetails>any())).thenReturn("ABC123");

        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail("jane.doe@example.org");
        signInRequest.setPassword("iloveyou");

        JwtAuthenticationResponse actualSignInResult = authenticationServiceImpl.signIn(signInRequest);

        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        verify(jwtService).generateRefreshToken(isA(Map.class), isA(UserDetails.class));
        verify(jwtService).generateToken(isA(UserDetails.class));
        verify(authenticationManager).authenticate(isA(Authentication.class));
        assertEquals("ABC123", actualSignInResult.getRefreshToken());
        assertEquals("ABC123", actualSignInResult.getToken());
    }

    /**
     * Method under test: {@link AuthenticationServiceImpl#signIn(SignInRequest)}
     */
    @Test
    void testSignIn2() throws AuthenticationException {
        // Arrange
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));

        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtService.generateToken(Mockito.<UserDetails>any())).thenThrow(new IllegalArgumentException("foo"));

        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail("jane.doe@example.org");
        signInRequest.setPassword("iloveyou");

        assertThrows(IllegalArgumentException.class, () -> authenticationServiceImpl.signIn(signInRequest));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        verify(jwtService).generateToken(isA(UserDetails.class));
        verify(authenticationManager).authenticate(isA(Authentication.class));
    }

    /**
     * Method under test: {@link AuthenticationServiceImpl#signIn(SignInRequest)}
     */
    @Test
    void testSignIn3() throws AuthenticationException {
        // Arrange
        when(authenticationManager.authenticate(Mockito.<Authentication>any()))
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(emptyResult);

        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail("jane.doe@example.org");
        signInRequest.setPassword("iloveyou");

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> authenticationServiceImpl.signIn(signInRequest));
        verify(userRepository).findByEmail(eq("jane.doe@example.org"));
        verify(authenticationManager).authenticate(isA(Authentication.class));
    }

    /**
     * Method under test:
     * {@link AuthenticationServiceImpl#refreshToken(RefreshTokenRequest)}
     */
    @Test
    void testRefreshToken() {

        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtService.generateToken(Mockito.<UserDetails>any())).thenReturn("ABC123");
        when(jwtService.isTokenValid(Mockito.<String>any(), Mockito.<UserDetails>any())).thenReturn(true);
        when(jwtService.extractUserName(Mockito.<String>any())).thenReturn("janedoe");

        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setToken("ABC123");

        JwtAuthenticationResponse actualRefreshTokenResult = authenticationServiceImpl.refreshToken(refreshTokenRequest);

        verify(userRepository).findByEmail(eq("janedoe"));
        verify(jwtService).extractUserName(eq("ABC123"));
        verify(jwtService).generateToken(isA(UserDetails.class));
        verify(jwtService).isTokenValid(eq("ABC123"), isA(UserDetails.class));
        assertEquals("ABC123", actualRefreshTokenResult.getRefreshToken());
        assertEquals("ABC123", actualRefreshTokenResult.getToken());
    }

    /**
     * Method under test:
     * {@link AuthenticationServiceImpl#refreshToken(RefreshTokenRequest)}
     */
    @Test
    void testRefreshToken2() {

        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtService.generateToken(Mockito.<UserDetails>any())).thenThrow(new IllegalArgumentException("foo"));
        when(jwtService.isTokenValid(Mockito.<String>any(), Mockito.<UserDetails>any())).thenReturn(true);
        when(jwtService.extractUserName(Mockito.<String>any())).thenReturn("janedoe");

        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setToken("ABC123");

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> authenticationServiceImpl.refreshToken(refreshTokenRequest));
        verify(userRepository).findByEmail(eq("janedoe"));
        verify(jwtService).extractUserName(eq("ABC123"));
        verify(jwtService).generateToken(isA(UserDetails.class));
        verify(jwtService).isTokenValid(eq("ABC123"), isA(UserDetails.class));
    }

    /**
     * Method under test:
     * {@link AuthenticationServiceImpl#refreshToken(RefreshTokenRequest)}
     */
    @Test
    void testRefreshToken3() {

        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(jwtService.isTokenValid(Mockito.<String>any(), Mockito.<UserDetails>any())).thenReturn(false);
        when(jwtService.extractUserName(Mockito.<String>any())).thenReturn("janedoe");

        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setToken("ABC123");

        // Act
        JwtAuthenticationResponse actualRefreshTokenResult = authenticationServiceImpl.refreshToken(refreshTokenRequest);

        // Assert
        verify(userRepository).findByEmail(eq("janedoe"));
        verify(jwtService).extractUserName(eq("ABC123"));
        verify(jwtService).isTokenValid(eq("ABC123"), isA(UserDetails.class));
        assertNull(actualRefreshTokenResult);
    }
}
