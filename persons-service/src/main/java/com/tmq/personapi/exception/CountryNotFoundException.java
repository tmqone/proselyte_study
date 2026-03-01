package com.tmq.personapi.exception;

public class CountryNotFoundException extends ApiException{
    public CountryNotFoundException(String message) {
        super(message);
    }
}
