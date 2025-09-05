package com.tmq.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PostNotFoundException extends GenericExceptionHandler {
    public PostNotFoundException(String message) {
        super(message);
    }
}
