package com.springsecurity.Spring_security.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignUpRequest {
    private int id;
    private String firstName;
    private String lastName;
    private long contact;
    private String about;
    private String email;
    private String password;

    private AddressDto addressDto;
}
