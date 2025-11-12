package com.tmq.exception;

public class AuthException extends GeneralException {
    public AuthException(String message) {
        super(message);
    }

    public AuthException(Exception e) {
        super(e);
    }

    public AuthException() {
        super();
    }
}
