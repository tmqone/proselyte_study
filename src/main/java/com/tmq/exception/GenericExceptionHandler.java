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
        throw new RuntimeException(e);
      }
    }
  }

  @SneakyThrows
  public GenericExceptionHandler(String message) {
    logStackTrace(FILE);
  }

  void logStackTrace(File FILE) throws IOException {
    try (FileWriter fileWriter = new FileWriter(FILE, true)) {
      for (StackTraceElement stackTraceElement : getStackTrace()) {
        fileWriter.append(stackTraceElement.toString()).append("\n");
      }
    } catch (IOException e) {
      throw new IOException(e);
    }
  }


}