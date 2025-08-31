package com.tmq.util;

import com.tmq.exception.GenericExceptionHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilesUtil {
    private static final String PATH = System.getProperty("user.home") + File.separator +
            ".tmq" + File.separator +
            "postingApp";

    public static String getPath() {
        return PATH;
    }

    public static void initFiles() {
        new File(getPath()).mkdirs();
        try {
            new File(FilesPath.LABEL.getFilePath()).createNewFile();
            new File(FilesPath.POST.getFilePath()).createNewFile();
            new File(FilesPath.WRITER.getFilePath()).createNewFile();
            new File(FilesPath.LOG.getFilePath()).createNewFile();
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }
}
