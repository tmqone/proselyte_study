package com.tmq.module_25.exception;


public class FileNotFoundException extends ApiException{
    public FileNotFoundException(String message) {
        super(message, "FILE_NOT_FOUND_EXCEPTION");
    }
}
