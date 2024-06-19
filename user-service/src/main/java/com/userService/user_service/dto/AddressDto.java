package com.userService.user_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AddressDto {

    private int id;

    @NotBlank(message = "Lane 1 cannot be null or empty.")
    private String lane1;

    @NotBlank(message = "Lane 1 cannot be null or empty.")
    private String lane2;

    @NotBlank(message = "City cannot be null or empty.")
    private String city;

    @NotBlank(message = "State cannot be null or empty.")
    private String state;

    private int zip;
}
