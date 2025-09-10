package com.tmq.exception;

import com.tmq.util.FilesPath;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class GenericExceptionHandler extends RuntimeException {


  private static final File FILE = new File(FilesPath.LOG.getFilePath());

  public GenericExceptionHandler(String message) {
    super(message);
    try {
      logStackTrace(FILE, this);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public GenericExceptionHandler() {
    super();
    try {
      logStackTrace(FILE, this);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

    void logStackTrace(File file, Throwable t) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            String newline = System.lineSeparator();
            fileWriter.append("[")
                    .append(LocalDateTime.now().toString())
                    .append("] ")
                    .append(t.getClass().getName())
                    .append(": ")
                    .append(t.getMessage() == null ? "" : t.getMessage())
                    .append(newline);

            for (StackTraceElement element : t.getStackTrace()) {
                fileWriter.append("\tat ")
                        .append(element.toString())
                        .append(newline);
            }
            Throwable cause = t.getCause();
            while (cause != null) {
                fileWriter.append("Caused by: ")
                        .append(cause.getClass().getName())
                        .append(": ")
                        .append(cause.getMessage() == null ? "" : cause.getMessage())
                        .append(newline);
                for (StackTraceElement element : cause.getStackTrace()) {
                    fileWriter.append("\tat ")
                            .append(element.toString())
                            .append(newline);
                }
                cause = cause.getCause();
            }
            fileWriter.append("--------------------------------------------------")
                    .append(newline);
        }
    }



}