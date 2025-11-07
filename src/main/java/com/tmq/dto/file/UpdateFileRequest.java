package com.tmq.dto.file;

public record UpdateFileRequest(Integer id, Integer userId, String name, String filePath) {
}
