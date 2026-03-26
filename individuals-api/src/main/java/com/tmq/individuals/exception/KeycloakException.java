package com.tmq.individuals.exception;

public class KeycloakException extends ApiException{
    public KeycloakException(String message, String code) {
        super(message, code);
    }
}
