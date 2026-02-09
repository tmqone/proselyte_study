package com.tmq.module_25.exception;

public class AWSException extends ApiException{
    public AWSException(String message, String errorCode) {
        super(message, errorCode);
    }
}
