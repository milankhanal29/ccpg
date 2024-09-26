package com.milan.codechangepresentationgenerator.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private final String message;
    private final String fieldName;
    private final Object fieldValue;

    public ResourceNotFoundException(String message, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : %s", message, fieldName, fieldValue));
        this.message = message;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException(String message) {
        this.message = message;
        this.fieldName = null;
        this.fieldValue = null;
    }
}