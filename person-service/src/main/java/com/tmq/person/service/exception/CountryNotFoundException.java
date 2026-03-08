package com.tmq.person.service.exception;

public class CountryNotFoundException extends ApiException{
    public CountryNotFoundException(String message) {
        super(message);
    }
}
