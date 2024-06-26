package com.springsecurity.Spring_security.service.impl;

import com.springsecurity.Spring_security.repository.UserRepository;
import com.springsecurity.Spring_security.service.LoadUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoadUserServiceImpl implements LoadUserService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsService(){
        return new UserDetailsService(){
            @Override
        public UserDetails loadUserByUsername(String userName){
            return userRepository.findByEmail(userName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userName));
        }
        };
    }
}
