package com.tmq.individuals_api.exception;

public class ApiException extends RuntimeException {
    String code;

    public ApiException(String message, String code) {
        super(message);
        this.code = code;
    }

}
