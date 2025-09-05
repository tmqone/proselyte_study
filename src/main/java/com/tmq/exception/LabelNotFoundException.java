package com.tmq.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class LabelNotFoundException extends GenericExceptionHandler{
    public LabelNotFoundException(String message) {
        super(message);
    }
}
