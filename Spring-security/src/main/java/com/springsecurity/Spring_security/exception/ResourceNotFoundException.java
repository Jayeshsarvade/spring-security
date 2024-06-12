package com.springsecurity.Spring_security.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException{

    String resourceName;
    String fieldName;
    long fieldValue;
    String value;

    public ResourceNotFoundException(String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, String Value) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, Value));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.value = Value;
    }
}
