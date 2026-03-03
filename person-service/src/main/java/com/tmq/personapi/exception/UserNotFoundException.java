package com.tmq.personapi.exception;

public class UserNotFoundException extends ApiException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
