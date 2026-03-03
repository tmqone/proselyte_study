package com.tmq.personapi.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
