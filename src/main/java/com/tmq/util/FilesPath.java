package com.tmq.util;

import lombok.AllArgsConstructor;

import java.io.File;
import java.nio.file.Files;

@AllArgsConstructor
public enum FilesPath{
    LABEL(FilesUtil.getPath() + File.separator + "labels.json"),
    POST(FilesUtil.getPath() + File.separator + "posts.json"),
    WRITER(FilesUtil.getPath() + File.separator + "writers.json"),
    LOG(FilesUtil.getPath() + File.separator + "logs.txt");

    private String filePath;

    public String getFilePath() {
        return filePath;
    }
}
