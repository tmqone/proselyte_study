package com.tmq.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NotCorrectInputException extends GenericExceptionHandler{
    public NotCorrectInputException(String message) {
        super(message);
    }
}
