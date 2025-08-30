package com.tmq.exception;

import java.io.IOException;

public class ObjectNotFoundException extends GenericExceptionHandler {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
