package com.tmq.module_25.exception;

public class EventNotFoundException extends ApiException{
    public EventNotFoundException() {
        super("Events not found", "EVENTS_NOT_FOUND");
    }
}
