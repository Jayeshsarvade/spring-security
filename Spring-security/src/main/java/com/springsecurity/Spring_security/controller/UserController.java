package com.springsecurity.Spring_security.controller;

import com.springsecurity.Spring_security.dto.SignUpRequest;
import com.springsecurity.Spring_security.dto.UserDto;
import com.springsecurity.Spring_security.entity.Role;
import com.springsecurity.Spring_security.payload.ApiResponse;
import com.springsecurity.Spring_security.payload.AppConstantsUser;
import com.springsecurity.Spring_security.payload.UserResponse;
import com.springsecurity.Spring_security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable int userId){
        UserDto userDto = userService.getUser(userId);
        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getAllUsers(
            @RequestParam(value = "pageNo", defaultValue = AppConstantsUser.PAGE_NO, required = false) Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstantsUser.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstantsUser.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstantsUser.SORT_DIR, required = false) String sortDir
    ){
        UserResponse userResponse = userService.getAllUser(pageNo, pageSize,sortBy,sortDir);
        return new ResponseEntity<UserResponse>(userResponse,HttpStatus.OK);
    }

    @GetMapping("/userRole")
    public ResponseEntity<List<UserDto>> getAllUsersByRole(@RequestParam Role role){
        List<UserDto> allUsersByRole = userService.getAllUsersByRole(role);
        return new ResponseEntity<List<UserDto>>(allUsersByRole,HttpStatus.OK);
    }

    @PutMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody SignUpRequest signUpRequest, @PathVariable Integer userId){
        UserDto updatedUSer = userService.updateUSer(signUpRequest, userId);
        return new ResponseEntity<>(updatedUSer,HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId){
        userService.deleteUser(userId);
        return new ResponseEntity<>(new ApiResponse("user deleted Successfully...", true), HttpStatus.OK);
    }
}
