package com.springsecurity.Spring_security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.springsecurity.Spring_security.dto.AddressDto;
import com.springsecurity.Spring_security.dto.SignUpRequest;
import com.springsecurity.Spring_security.dto.UserDto;
import com.springsecurity.Spring_security.entity.Role;
import com.springsecurity.Spring_security.entity.User;
import com.springsecurity.Spring_security.exception.ResourceNotFoundException;
import com.springsecurity.Spring_security.openfeignclient.AddressClient;
import com.springsecurity.Spring_security.payload.UserResponse;
import com.springsecurity.Spring_security.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import com.springsecurity.Spring_security.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserServiceImpl.class, PasswordEncoder.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class UserServiceImplTest {
    @MockBean
    private AddressClient addressClient;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    /**
     * Method under test: {@link UserServiceImpl#getUser(int)}
     */
    private AddressDto addressDto;

    private User user;

    private User user2;

    @BeforeEach
    void setUp() throws Exception {
        addressDto = AddressDto.builder().city("Oxford").id(1).lane1("Lane1").lane2("Lane2").state("MD").zip(1).build();

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

        user2 = new User();
        user2.setAbout("asc");
        user2.setComments(new HashSet<>());
        user2.setContact(1L);
        user2.setEmail("john.smith@example.org");
        user2.setFirstName("John");
        user2.setId(2);
        user2.setLastName("Smith");
        user2.setPassword("Fetching all users with pageNo: {}, pageSize: {}, sortBy: {}, sortDir: {}");
        user2.setPost(new ArrayList<>());
        user2.setRole(Role.ADMIN);
    }

    @Test
    void testGetUser() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        // Act
        UserDto actualUser = userServiceImpl.getUser(1);

        // Assert
        verify(addressClient).getAddressByUserId(eq(1));
        verify(userRepository).findById(eq(1));
        assertEquals("About", actualUser.getAbout());
        assertEquals("Doe", actualUser.getLastName());
        assertEquals("Jane", actualUser.getFirstName());
        assertEquals("Oxford", actualUser.getAddressDto().getCity());
        assertEquals("iloveyou", actualUser.getPassword());
        assertEquals("jane.doe@example.org", actualUser.getEmail());
        assertEquals(1, actualUser.getId().intValue());
        assertEquals(1L, actualUser.getContact());
        assertEquals(Role.USER, actualUser.getRole());
    }

    /**
     * Method under test: {@link UserServiceImpl#getUser(int)}
     */
    @Test
    void testGetUser2() {
        // Arrange
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.getUser(1));
        verify(userRepository).findById(eq(1));
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#getAllUser(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllUser() {
        // Arrange
        ArrayList<User> content = new ArrayList<>();
        when(userRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(content));

        UserResponse actualAllUser = userServiceImpl.getAllUser(1, 3, "Sort By", "Sort Dir");

        verify(userRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllUser.getPageNo());
        assertEquals(0, actualAllUser.getPageSize());
        assertEquals(0L, actualAllUser.getTotalElement());
        assertEquals(1, actualAllUser.getTotalPages());
        assertTrue(actualAllUser.isLastPage());
        assertEquals(content, actualAllUser.getContent());
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#getAllUser(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllUser2() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<User> content = new ArrayList<>();
        content.add(user);
        PageImpl<User> pageImpl = new PageImpl<>(content);
        when(userRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        UserResponse actualAllUser = userServiceImpl.getAllUser(1, 3, "Sort By", "Sort Dir");

        verify(addressClient).getAddressByUserId(eq(1));
        verify(userRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllUser.getPageNo());
        assertEquals(1, actualAllUser.getPageSize());
        assertEquals(1, actualAllUser.getTotalPages());
        assertEquals(1, actualAllUser.getContent().size());
        assertEquals(1L, actualAllUser.getTotalElement());
        assertTrue(actualAllUser.isLastPage());
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#getAllUser(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllUser3() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<User> content = new ArrayList<>();
        content.add(user2);
        content.add(user);
        PageImpl<User> pageImpl = new PageImpl<>(content);
        when(userRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        UserResponse actualAllUser = userServiceImpl.getAllUser(1, 3, "Sort By", "Sort Dir");

        verify(addressClient, atLeast(1)).getAddressByUserId(anyInt());
        verify(userRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllUser.getPageNo());
        assertEquals(1, actualAllUser.getTotalPages());
        assertEquals(2, actualAllUser.getPageSize());
        assertEquals(2, actualAllUser.getContent().size());
        assertEquals(2L, actualAllUser.getTotalElement());
        assertTrue(actualAllUser.isLastPage());
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#getAllUser(Integer, Integer, String, String)}
     */
    @Test
    void testGetAllUser4() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<User> content = new ArrayList<>();
        content.add(user);
        PageImpl<User> pageImpl = new PageImpl<>(content);
        when(userRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        UserResponse actualAllUser = userServiceImpl.getAllUser(1, 3, "Sort By", "asc");

        verify(addressClient).getAddressByUserId(eq(1));
        verify(userRepository).findAll(isA(Pageable.class));
        assertEquals(0, actualAllUser.getPageNo());
        assertEquals(1, actualAllUser.getPageSize());
        assertEquals(1, actualAllUser.getTotalPages());
        assertEquals(1, actualAllUser.getContent().size());
        assertEquals(1L, actualAllUser.getTotalElement());
        assertTrue(actualAllUser.isLastPage());
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#getAllUsersByRole(Role, int, int, String, String)}
     */
    @Test
    void testGetAllUsersByRole() {
        // Arrange
        ArrayList<User> content = new ArrayList<>();
        when(userRepository.findByRole(Mockito.<Role>any(), Mockito.<Pageable>any())).thenReturn(new PageImpl<>(content));

        // Act
        UserResponse actualAllUsersByRole = userServiceImpl.getAllUsersByRole(Role.USER, 1, 3, "Sort By", "Sort Dir");

        // Assert
        verify(userRepository).findByRole(eq(Role.USER), isA(Pageable.class));
        assertEquals(0, actualAllUsersByRole.getPageNo());
        assertEquals(0, actualAllUsersByRole.getPageSize());
        assertEquals(0L, actualAllUsersByRole.getTotalElement());
        assertEquals(1, actualAllUsersByRole.getTotalPages());
        assertTrue(actualAllUsersByRole.isLastPage());
        assertEquals(content, actualAllUsersByRole.getContent());
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#getAllUsersByRole(Role, int, int, String, String)}
     */
    @Test
    void testGetAllUsersByRole2() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<User> content = new ArrayList<>();
        content.add(user);
        PageImpl<User> pageImpl = new PageImpl<>(content);
        when(userRepository.findByRole(Mockito.<Role>any(), Mockito.<Pageable>any())).thenReturn(pageImpl);

        UserResponse actualAllUsersByRole = userServiceImpl.getAllUsersByRole(Role.USER, 1, 3, "Sort By", "Sort Dir");

        verify(addressClient).getAddressByUserId(eq(1));
        verify(userRepository).findByRole(eq(Role.USER), isA(Pageable.class));
        assertEquals(0, actualAllUsersByRole.getPageNo());
        assertEquals(1, actualAllUsersByRole.getPageSize());
        assertEquals(1, actualAllUsersByRole.getTotalPages());
        assertEquals(1, actualAllUsersByRole.getContent().size());
        assertEquals(1L, actualAllUsersByRole.getTotalElement());
        assertTrue(actualAllUsersByRole.isLastPage());
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#getAllUsersByRole(Role, int, int, String, String)}
     */
    @Test
    void testGetAllUsersByRole3() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<User> content = new ArrayList<>();
        content.add(user);
        PageImpl<User> pageImpl = new PageImpl<>(content);
        when(userRepository.findByRole(Mockito.<Role>any(), Mockito.<Pageable>any())).thenReturn(pageImpl);

        UserResponse actualAllUsersByRole = userServiceImpl.getAllUsersByRole(Role.ADMIN, 1, 3, "Sort By", "Sort Dir");

        verify(addressClient).getAddressByUserId(eq(1));
        verify(userRepository).findByRole(eq(Role.ADMIN), isA(Pageable.class));
        assertEquals(0, actualAllUsersByRole.getPageNo());
        assertEquals(1, actualAllUsersByRole.getPageSize());
        assertEquals(1, actualAllUsersByRole.getTotalPages());
        assertEquals(1, actualAllUsersByRole.getContent().size());
        assertEquals(1L, actualAllUsersByRole.getTotalElement());
        assertTrue(actualAllUsersByRole.isLastPage());
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#getAllUsersByRole(Role, int, int, String, String)}
     */
    @Test
    void testGetAllUsersByRole4() {

        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        ArrayList<User> content = new ArrayList<>();
        content.add(user);
        PageImpl<User> pageImpl = new PageImpl<>(content);
        when(userRepository.findByRole(Mockito.<Role>any(), Mockito.<Pageable>any())).thenReturn(pageImpl);

        UserResponse actualAllUsersByRole = userServiceImpl.getAllUsersByRole(Role.USER, 1, 3, "Sort By", "asc");

        verify(addressClient).getAddressByUserId(eq(1));
        verify(userRepository).findByRole(eq(Role.USER), isA(Pageable.class));
        assertEquals(0, actualAllUsersByRole.getPageNo());
        assertEquals(1, actualAllUsersByRole.getPageSize());
        assertEquals(1, actualAllUsersByRole.getTotalPages());
        assertEquals(1, actualAllUsersByRole.getContent().size());
        assertEquals(1L, actualAllUsersByRole.getTotalElement());
        assertTrue(actualAllUsersByRole.isLastPage());
    }

    /**
     * Method under test: {@link UserServiceImpl#updateUser(SignUpRequest, Integer)}
     */
    @Test
    void testUpdateUser() {
        // Arrange
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");
        when(addressClient.getAddressByUserId(anyInt())).thenReturn(addressDto);

        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user));
        SignUpRequest signUpRequest = mock(SignUpRequest.class);
        when(signUpRequest.getAbout()).thenReturn("About");
        when(signUpRequest.getEmail()).thenReturn("jane.doe@example.org");
        when(signUpRequest.getFirstName()).thenReturn("Jane");
        when(signUpRequest.getLastName()).thenReturn("Doe");
        when(signUpRequest.getPassword()).thenReturn("iloveyou");
        when(signUpRequest.getContact()).thenReturn(1L);

        SignUpRequest actualUpdateUserResult = userServiceImpl.updateUser(signUpRequest, 1);

        verify(signUpRequest).getAbout();
        verify(signUpRequest).getContact();
        verify(signUpRequest).getEmail();
        verify(signUpRequest).getFirstName();
        verify(signUpRequest).getLastName();
        verify(signUpRequest).getPassword();
        verify(addressClient).getAddressByUserId(eq(1));
        verify(userRepository).findById(eq(1));
        verify(userRepository).save(isA(User.class));
        verify(passwordEncoder).encode(isA(CharSequence.class));
        assertEquals("About", actualUpdateUserResult.getAbout());
        assertEquals("Doe", actualUpdateUserResult.getLastName());
        assertEquals("Jane", actualUpdateUserResult.getFirstName());
        assertEquals("Oxford", actualUpdateUserResult.getAddressDto().getCity());
        assertEquals("jane.doe@example.org", actualUpdateUserResult.getEmail());
        assertEquals("secret", actualUpdateUserResult.getPassword());
        assertEquals(1, actualUpdateUserResult.getId().intValue());
        assertEquals(1L, actualUpdateUserResult.getContact());
        assertEquals(Role.USER, actualUpdateUserResult.getRole());
    }

    /**
     * Method under test: {@link UserServiceImpl#updateUser(SignUpRequest, Integer)}
     */
    @Test
    void testUpdateUser2() {

        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(Optional.of(user));
        SignUpRequest signUpRequest = mock(SignUpRequest.class);
        when(signUpRequest.getFirstName())
                .thenThrow(new ResourceNotFoundException("Updating user with Id {}: {}", "Updating user with Id {}: {}", 42L));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.updateUser(signUpRequest, 1));
        verify(signUpRequest).getFirstName();
        verify(userRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link UserServiceImpl#deleteUser(Integer)}
     */
    @Test
    void testDeleteUser() {
        doNothing().when(addressClient).deleteAddressByUserId(anyInt());

        Optional<User> ofResult = Optional.of(user);
        doNothing().when(userRepository).delete(Mockito.<User>any());
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(ofResult);

        userServiceImpl.deleteUser(1);

        verify(addressClient).deleteAddressByUserId(eq(1));
        verify(userRepository).delete(isA(User.class));
        verify(userRepository).findById(eq(1));
    }

    /**
     * Method under test: {@link UserServiceImpl#deleteUser(Integer)}
     */
    @Test
    void testDeleteUser2() {
        // Arrange
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Integer>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.deleteUser(1));
        verify(userRepository).findById(eq(1));
    }
}
