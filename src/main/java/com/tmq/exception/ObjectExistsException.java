package com.tmq.exception;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ObjectExistsException extends GenericExceptionHandler{
    public ObjectExistsException(String message) {
        super(message);
    }
}
