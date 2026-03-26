package com.tmq.person.service.exception;

public class UserNotFoundException extends ApiException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
