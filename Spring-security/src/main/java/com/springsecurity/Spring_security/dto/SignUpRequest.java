package com.springsecurity.Spring_security.dto;

import lombok.Data;

@Data
public class SignUpRequest {
    private String firstName;
    private String lastName;
    private long contact;
    private String about;
    private String email;
    private String password;
}
