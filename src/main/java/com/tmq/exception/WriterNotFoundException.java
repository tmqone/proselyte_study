package com.tmq.exception;

import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor
public class WriterNotFoundException extends GenericExceptionHandler {
    public WriterNotFoundException(String message) {
        super(message);
    }
}
