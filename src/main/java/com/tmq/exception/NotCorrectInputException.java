package com.tmq.exception;

public class NotCorrectInputException extends GeneralException{
    public NotCorrectInputException(String message) {
        super(message);
    }

    public NotCorrectInputException(Exception e) {
        super(e);
    }

    public NotCorrectInputException() {
    }
}
