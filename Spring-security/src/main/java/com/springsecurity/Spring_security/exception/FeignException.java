package com.springsecurity.Spring_security.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeignException extends RuntimeException{

	private int fieldValue;
	private String message;
	
	public FeignException( String message,int fieldValue) {
		super(String.format("address not found with UserId: %s", fieldValue));
		this.message = message;
	}
	
	
}
