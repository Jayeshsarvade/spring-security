package com.springsecurity.Spring_security.dto;

import com.springsecurity.Spring_security.entity.Role;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDto {

    private Integer id;
    @NotBlank(message = "FirstName cannot be blank")
    @Size(min = 2, max = 50, message = "FirstName must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "LastName cannot be blank")
    @Size(min = 2, max = 50, message = "LastName must be between 2 and 50 characters")
    private String lastName;

    @DecimalMin(value = "1000000000", inclusive = true, message = "Contact number must be at least 10 digits")
    @DecimalMax(value = "999999999999999", inclusive = true, message = "Contact number cannot exceed 15 digits")
    private long contact;

    @NotBlank(message = "About field cannot be blank")
    @Size(max = 500, message = "About field must be less than or equal to 250 characters")
    private String about;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 2, message = "Password must be at least 2 characters long")
    private String password;

    private Role role;
}
