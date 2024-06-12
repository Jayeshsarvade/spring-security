package com.springsecurity.Spring_security.service.impl;

import com.springsecurity.Spring_security.dto.SignUpRequest;
import com.springsecurity.Spring_security.dto.UserDto;
import com.springsecurity.Spring_security.entity.Role;
import com.springsecurity.Spring_security.entity.User;
import com.springsecurity.Spring_security.exception.ResourceNotFoundException;
import com.springsecurity.Spring_security.payload.UserResponse;
import com.springsecurity.Spring_security.repository.UserRepository;
import com.springsecurity.Spring_security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto getUser(int userId) {
        logger.info("Fetching user with Id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));
        logger.debug("User found: {}", user);

        UserDto userDto = UserDto.builder().id(user.getId()).firstName(user.getFirstName())
                .lastName(user.getLastName())
                .contact(user.getContact())
                .about(user.getAbout())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole()).build();
        return userDto;
    }

    @Override
    public UserResponse getAllUser(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
        logger.info("Fetching all users with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}", pageNo, pageSize,
                sortBy, sortDir);
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> pagePost = userRepository.findAll(pageable);
        List<User> content = pagePost.getContent();

        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : content) {
            UserDto userDto = UserDto.builder().id(user.getId()).firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .contact(user.getContact())
                    .about(user.getAbout())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .role(user.getRole()).build();
            userDtoList.add(userDto);
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setContent(userDtoList);
        userResponse.setPageNo(pagePost.getNumber());
        userResponse.setPageSize(pagePost.getSize());
        userResponse.setTotalElement(pagePost.getTotalElements());
        userResponse.setTotalPages(pagePost.getTotalPages());
        userResponse.setLastPage(pagePost.isLast());
        logger.info("Returning user response: {}", userResponse.toString());
        return userResponse;
    }

    @Override
    public List<UserDto> getAllUsersByRole(Role role) {

        List<User> users = userRepository.findByRole(role);

        List<UserDto> userDtoList = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = UserDto.builder().id(user.getId()).firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .contact(user.getContact())
                    .about(user.getAbout())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .role(user.getRole()).build();
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    @Override
    public UserDto updateUSer(SignUpRequest signUpRequest, Integer userId) {
        logger.info("Updating user with Id {}: {}", userId, signUpRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));

        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setContact(signUpRequest.getContact());
        user.setAbout(signUpRequest.getAbout());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        //        user.setRole(Role.USER);
        User save = userRepository.save(user);
        logger.info("User updated successfully: {}", save);

        return UserDto.builder().id(user.getId()).firstName(user.getFirstName())
                .lastName(user.getLastName())
                .contact(user.getContact())
                .about(user.getAbout())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole()).build();
    }

    @Override
    public void deleteUser(Integer userId) {
        logger.info("Deleting user with Id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));
        userRepository.delete(user);
        logger.info("User deleted successfully...");
    }
}
