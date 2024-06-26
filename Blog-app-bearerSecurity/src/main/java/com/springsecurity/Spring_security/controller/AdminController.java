package com.springsecurity.Spring_security.controller;

import com.springsecurity.Spring_security.dto.UserDto;
import com.springsecurity.Spring_security.entity.Role;
import com.springsecurity.Spring_security.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/adminRole")
    public ResponseEntity<UserDto> getAdmin(@RequestParam(required = false) Role role){
        UserDto admin = adminService.getAdmin(role);
            if (admin!= null) {
                return new ResponseEntity<>(admin, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
