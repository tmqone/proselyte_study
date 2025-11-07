package com.tmq.exception;

public class FileNotFoundException extends GeneralException{
    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(Exception e) {
        super(e);
    }

    public FileNotFoundException() {
        super();
    }
}
