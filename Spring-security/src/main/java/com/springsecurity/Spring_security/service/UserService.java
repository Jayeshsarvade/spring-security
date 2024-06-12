package com.springsecurity.Spring_security.service;

import com.springsecurity.Spring_security.dto.SignUpRequest;
import com.springsecurity.Spring_security.dto.UserDto;
import com.springsecurity.Spring_security.entity.Role;
import com.springsecurity.Spring_security.payload.UserResponse;

import java.util.List;

public interface UserService {

    UserDto getUser(int userId);
    UserResponse getAllUser(Integer pageNo, Integer pageSize, String sortBy, String sortDir);
    List<UserDto> getAllUsersByRole(Role role);
    public UserDto updateUSer(SignUpRequest signUpRequest, Integer userId);
    public void deleteUser(Integer userId);
}
