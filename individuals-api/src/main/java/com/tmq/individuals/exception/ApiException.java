package com.tmq.individuals.exception;

public class ApiException extends RuntimeException {
    String code;

    public ApiException(String message, String code) {
        super(message);
        this.code = code;
    }

}
