package com.springsecurity.Spring_security.service.impl;

import com.springsecurity.Spring_security.dto.UserDto;
import com.springsecurity.Spring_security.entity.Role;
import com.springsecurity.Spring_security.entity.User;
import com.springsecurity.Spring_security.repository.UserRepository;
import com.springsecurity.Spring_security.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto getAdmin(Role role) {
        List<User> byRole = userRepository.findByRole(Role.ADMIN);
        UserDto adminDto = null;
        for (User user : byRole) {
            adminDto = UserDto.builder().id(user.getId()).firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .contact(user.getContact())
                    .about(user.getAbout())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .role(user.getRole()).build();
        }
        return adminDto;
    }
}
