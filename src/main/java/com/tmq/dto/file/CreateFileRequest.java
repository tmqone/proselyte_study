package com.tmq.dto.file;

import jakarta.servlet.http.Part;

import java.util.Collection;

public record CreateFileRequest (Integer userId, Collection<Part> parts) {
}
