package com.tmq.module_25.controller;

import com.tmq.module_25.dto.FileDto;
import com.tmq.module_25.mapper.FileMapper;
import com.tmq.module_25.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.image.DataBuffer;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileRestControllerV1 {
    private final FileService fileService;
    private final FileMapper fileMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<FileDto> uploadFile(@RequestPart("file-data") FilePart filePart) {
        return fileService.createFile(filePart).map(fileMapper::map);
    }

    @GetMapping
    public Mono<FileDto> getFileInfo(@RequestParam("id") Long id, Authentication authentication) {
        return fileService.getFileInfo(id).map(fileMapper::map);
    }

    @GetMapping("/download")
    public Mono<Flux<DataBuffer>> downloadFile(@RequestParam("id") Long id, Authentication authentication) {
        return null;
    }
}
