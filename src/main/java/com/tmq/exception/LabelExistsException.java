package com.tmq.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LabelExistsException extends GenericExceptionHandler{
    public LabelExistsException(String message) {
        super(message);
    }
}
