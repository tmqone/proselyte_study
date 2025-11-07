package com.tmq.exception;

public class FileExistsException extends GeneralException {
    public FileExistsException(String message) {
        super(message);
    }

    public FileExistsException(Exception e) {
        super(e);
    }

    public FileExistsException() {
        super();
    }
}
