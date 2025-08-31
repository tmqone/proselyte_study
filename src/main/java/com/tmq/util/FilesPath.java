package com.tmq.util;

import lombok.AllArgsConstructor;

import java.io.File;

@AllArgsConstructor
public enum FilesPath{
    LABEL(FilesUtil.getPath() + File.separator + "labels.json"),
    POST(FilesUtil.getPath() + File.separator + "posts.json"),
    WRITER(FilesUtil.getPath() + File.separator + "writers.json");

    private String filePath;

    public String getFilePath() {
        return filePath;
    }
}
