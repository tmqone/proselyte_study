package com.tmq.util;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class InitUtil implements BeforeAllCallback {
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        delete(new File(System.getProperty("user.dir") + "/test_uploads"));
        copyFolder(Path.of(System.getProperty("user.dir") + "/test_files"),
                Path.of(System.getProperty("user.dir") + "/test_uploads"));
        PropertiesUtil.loadProperties();
        HibernateUtil.initHibernate();
        FlywayUtil.migrate();
    }

    private void copyFolder(Path src, Path dest) throws IOException {
        try (Stream<Path> stream = Files.walk(src)) {
            stream.forEach(source -> copy(source, dest.resolve(src.relativize(source))));
        }
    }

    private void copy(Path source, Path dest) {
        try {
            Files.copy(source, dest, REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                delete(c);
            }
            f.delete();
        } else {
            f.delete();
        }
    }
}
