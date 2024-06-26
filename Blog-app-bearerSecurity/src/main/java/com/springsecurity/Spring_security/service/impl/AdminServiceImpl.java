package com.springsecurity.Spring_security.service.impl;

import com.springsecurity.Spring_security.dto.AddressDto;
import com.springsecurity.Spring_security.dto.UserDto;
import com.springsecurity.Spring_security.entity.Role;
import com.springsecurity.Spring_security.entity.User;
import com.springsecurity.Spring_security.openfeignclient.AddressClient;
import com.springsecurity.Spring_security.repository.UserRepository;
import com.springsecurity.Spring_security.service.AdminService;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressClient addressClient;

    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

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
            try {
                AddressDto addressDto = addressClient.getAddressByUserId(byRole.get(0).getId());
                adminDto.setAddressDto(addressDto);
            } catch (FeignException.NotFound ex) {
                String errorMessage = String.format("address not found with UserId: %d", byRole.get(0).getId());
                logger.error(errorMessage);
                adminDto.setAddressDto(null);
            }
        }
        return adminDto;
    }
}
