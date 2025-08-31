package com.tmq.exception;

import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenericExceptionHandler extends RuntimeException {


  private static final File PATH = new File(System.getProperty("user.home") + "\\.tmq\\postingApp");
  private static final File FILE = new File(PATH + "\\logs.txt");

  static {
    if (!PATH.exists()) {
      PATH.mkdirs();
    }

    if (!FILE.exists()) {
      try {
        FILE.createNewFile();
      } catch (IOException e) {
        throw new GenericExceptionHandler(e.getMessage());
      }
    }
  }

  @SneakyThrows
  public GenericExceptionHandler(String message) {
    logStackTrace(FILE);
  }

  void logStackTrace(File file) throws IOException {
    try (FileWriter fileWriter = new FileWriter(file, true)) {
      fileWriter.append(getLocalizedMessage()).append("\n");
      for (StackTraceElement stackTraceElement : getStackTrace()) {
        fileWriter.append(stackTraceElement.toString()).append("\n");
      }
      fileWriter.append((char) Character.LINE_SEPARATOR);
    } catch (IOException e) {
      throw new IOException(e);
    }
  }


}