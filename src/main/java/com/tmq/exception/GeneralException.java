package com.tmq.exception;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GeneralException extends RuntimeException {
    public GeneralException(String message) {
        super(message);
    }

    public GeneralException(Exception e) {
        super(e);
    }

    public GeneralException() {
        super();
    }
}
