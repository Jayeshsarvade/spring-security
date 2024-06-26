package com.springsecurity.Spring_security.service.impl;

import com.springsecurity.Spring_security.dto.AddressDto;
import com.springsecurity.Spring_security.dto.SignUpRequest;
import com.springsecurity.Spring_security.dto.UserDto;
import com.springsecurity.Spring_security.entity.Role;
import com.springsecurity.Spring_security.entity.User;
import com.springsecurity.Spring_security.exception.ResourceNotFoundException;
import com.springsecurity.Spring_security.openfeignclient.AddressClient;
import com.springsecurity.Spring_security.payload.UserResponse;
import com.springsecurity.Spring_security.repository.UserRepository;
import com.springsecurity.Spring_security.service.UserService;
import feign.FeignException;
import jakarta.transaction.Transactional;
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
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private AddressClient addressClient;

    /**
     * This method retrieves a user by their unique identifier.
     *
     * @param userId The unique identifier of the user to be fetched.
     * @return A UserDto object containing the details of the fetched user.
     * @throws FeignException If an error occurs while deleting the address associated with the user.
     * @throws ResourceNotFoundException If the user with the given userId is not found.
     */
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
        try {
            AddressDto addressDto = addressClient.getAddressByUserId(userId);
            userDto.setAddressDto(addressDto);
        } catch (FeignException.NotFound ex) {
            String errorMessage = String.format("address not found with UserId: %d", userId);
            logger.error(errorMessage);
            userDto.setAddressDto(null);
        }
        return userDto;
    }

    /**
     * This method retrieves all users from the system with pagination, sorting, and filtering.
     *
     * @param pageNo The page number to retrieve.
     * @param pageSize The number of users to retrieve per page.
     * @param sortBy The field to sort the users by.
     * @param sortDir The direction to sort the users (asc or desc).
     * @return A UserResponse object containing the details of the retrieved users.
     */
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
            AddressDto addressDto = null;
            try {
                addressDto = addressClient.getAddressByUserId(user.getId());
            } catch (FeignException.NotFound ex) {
                logger.error("address not found for user with Id: {}", user.getId());
            }
            UserDto userDto = UserDto.builder().id(user.getId()).firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .contact(user.getContact())
                    .about(user.getAbout())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .role(user.getRole()).build();
            userDto.setAddressDto(addressDto);
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

    /**
     * This method retrieves all users from the system based on the given role.
     *
     * @param role The role to filter the users by.
     * @return A list of UserDto objects containing the details of the retrieved users.
     * @throws FeignException If an error occurs while fetching the address associated with a user.
     */
    @Override
    public UserResponse getAllUsersByRole(Role role, int pageNo, int pageSize, String sortBy,String sortDir) {

        logger.info("Fetching all users with role: {} with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}", role, pageNo, pageSize, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> pageUsers = userRepository.findByRole(role, pageable);
        List<User> users = pageUsers.getContent();

        List<UserDto> userDtoList = new ArrayList<>();

        for (User user : users) {
            AddressDto addressDto = null;
            try {
                addressDto = addressClient.getAddressByUserId(user.getId());
            } catch (FeignException.NotFound ex) {
                logger.error("Address not found for user with Id: {}", user.getId());
            }
            UserDto userDto = UserDto.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .contact(user.getContact())
                    .about(user.getAbout())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .role(user.getRole())
                    .addressDto(addressDto)
                    .build();

            userDtoList.add(userDto);
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setContent(userDtoList);
        userResponse.setPageNo(pageUsers.getNumber());
        userResponse.setPageSize(pageUsers.getSize());
        userResponse.setTotalElement(pageUsers.getTotalElements());
        userResponse.setTotalPages(pageUsers.getTotalPages());
        userResponse.setLastPage(pageUsers.isLast());

        logger.info("Returning user response for role {}: {}", role, userResponse);
        return userResponse;
    }
    /**
     * This method updates a user in the system.
     *
     * @param signUpRequest The DTO object containing the updated details of the user.
     * @param userId The unique identifier of the user to be updated.
     * @return A UserDto object containing the updated details of the user.
     * @throws FeignException If an error occurs while deleting the address associated with the user.
     * @throws ResourceNotFoundException If the user with the given userId is not found.
     */
    @Override
    public SignUpRequest updateUser(SignUpRequest signUpRequest, Integer userId) {
        logger.info("Updating user with Id {}: {}", userId, signUpRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setContact(signUpRequest.getContact());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(Role.USER);
        user.setAbout(signUpRequest.getAbout());
        User save = userRepository.save(user);
        logger.info("User updated successfully: {}", save);

        AddressDto addressByUserId = null;
        try {
            addressByUserId = addressClient.getAddressByUserId(userId);
        } catch (FeignException.NotFound ex) {
            String errorMessage = String.format("address not found with UserId: %d", userId);
            logger.error(errorMessage);
            signUpRequest.setAddressDto(null);
        }
        SignUpRequest signUpRequest2 = SignUpRequest.builder().id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .contact(user.getContact())
                .email(user.getEmail())
                .password(user.getPassword()).about(user.getAbout())
                .role(user.getRole()).build();
        signUpRequest2.setAddressDto(addressByUserId);
        return signUpRequest2;
    }

    /**
     * This method deletes a user in the system.
     *
     * @param userId The unique identifier of the user to be deleted.
     * @throws ResourceNotFoundException If the user with the given userId is not found.
     * @throws FeignException If an error occurs while deleting the address associated with the user.
     */
    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        logger.info("Deleting user with Id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));
        try {
            addressClient.deleteAddressByUserId(userId);
        } catch (FeignException.NotFound ex) {
            logger.warn("Address not found for user with Id: {}, proceeding with user deletion", userId);
        } catch (FeignException ex) {
            logger.error("Error occurred while deleting address for user with Id: {}", userId, ex);
            throw ex;
        }
        userRepository.delete(user);

        logger.info("User deleted successfully...");
    }
}
