package com.springsecurity.Spring_security.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface LoadUserService {
    UserDetailsService userDetailsService();
}
