package com.tmq.individuals_api.exception;

public class ValidationException extends ApiException{
    public ValidationException(String message) {
        super(message, "VALIDATION_EXCEPTION");
    }
}
