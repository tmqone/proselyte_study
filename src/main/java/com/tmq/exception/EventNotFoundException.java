package com.tmq.exception;

public class EventNotFoundException extends GeneralException {
    public EventNotFoundException(String message) {
        super(message);
    }

    public EventNotFoundException(Exception e) {
        super(e);
    }

    public EventNotFoundException() {
        super();
    }
}
