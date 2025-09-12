package com.tmq.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class WriterNotFoundException extends GenericExceptionHandler {
    public WriterNotFoundException(String message) {
        super(message);
    }
}
