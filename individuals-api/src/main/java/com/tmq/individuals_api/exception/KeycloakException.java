package com.tmq.individuals_api.exception;

public class KeycloakException extends ApiException{
    public KeycloakException(String message, String code) {
        super(message, code);
    }
}
