package com.springsecurity.Spring_security.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    private int id;
    private String lane1;
    private String lane2;
    private String city;
    private String state;
    private int zip;
}
