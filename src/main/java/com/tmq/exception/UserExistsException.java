package com.tmq.exception;

public class UserExistsException extends GeneralException{
    public UserExistsException(String message) {
        super(message);
    }

    public UserExistsException(Exception e) {
        super(e);
    }

    public UserExistsException() {
        super();
    }
}
