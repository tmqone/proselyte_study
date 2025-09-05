package com.tmq.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PostExistsException extends GenericExceptionHandler{
    public PostExistsException(String message) {
        super(message);
    }
}
