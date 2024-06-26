package com.springsecurity.Spring_security.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse {

	private String message;
    private boolean success;
}
