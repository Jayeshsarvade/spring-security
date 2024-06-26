package com.springsecurity.Spring_security.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserNotFoundException extends RuntimeException{
    String message = "User not found";

    public UserNotFoundException(String message) {
        super(String.format("User %s not found", message));
    }
}
