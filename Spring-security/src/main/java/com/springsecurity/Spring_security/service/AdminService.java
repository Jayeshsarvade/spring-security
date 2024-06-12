package com.springsecurity.Spring_security.service;

import com.springsecurity.Spring_security.dto.UserDto;
import com.springsecurity.Spring_security.entity.Role;

public interface AdminService {

    UserDto getAdmin(Role role);
}
