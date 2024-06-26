package com.springsecurity.Spring_security.dto;

import com.springsecurity.Spring_security.entity.Role;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDto implements UserDetails {

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

    private AddressDto addressDto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
