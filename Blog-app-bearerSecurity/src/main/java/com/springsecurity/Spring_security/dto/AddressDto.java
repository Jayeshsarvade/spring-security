package com.springsecurity.Spring_security.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    private int id;

    @NotBlank(message = "Lane1 cannot be blank")
    @Size(max = 255, message = "Lane1 cannot exceed 255 characters")
    private String lane1;

    @Size(max = 255, message = "Lane2 cannot exceed 255 characters")
    private String lane2;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @NotBlank(message = "State cannot be blank")
    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;

    @NotNull(message = "Zip code cannot be null")
    @Min(value = 10000, message = "Zip code must be at least 5 digits")
    @Max(value = 99999, message = "Zip code cannot exceed 5 digits")
    private int zip;
}
