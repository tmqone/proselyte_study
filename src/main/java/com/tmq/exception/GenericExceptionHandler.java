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
      logStackTrace(FILE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public GenericExceptionHandler() {
    super();
    try {
      logStackTrace(FILE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  void logStackTrace(File file) throws IOException {
    try (FileWriter fileWriter = new FileWriter(file, true)) {
      fileWriter.append(getMessage()).append("\n");
      for (StackTraceElement stackTraceElement : getStackTrace()) {
        fileWriter
                .append(LocalDateTime.now().toString())
                .append(" ")
                .append(stackTraceElement.toString())
                .append("\n");
      }
      fileWriter.append((char) Character.LINE_SEPARATOR);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }


}