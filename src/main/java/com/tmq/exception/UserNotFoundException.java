package com.tmq.exception;

public class UserNotFoundException extends GeneralException{
    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(Exception e) {
        super(e);
    }

    public UserNotFoundException() {
    }
}
