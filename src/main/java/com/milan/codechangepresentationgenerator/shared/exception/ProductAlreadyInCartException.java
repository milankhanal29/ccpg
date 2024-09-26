package com.milan.codechangepresentationgenerator.shared.exception;

public class ProductAlreadyInCartException extends RuntimeException {

    public ProductAlreadyInCartException(String message) {
        super(message);
    }
}
