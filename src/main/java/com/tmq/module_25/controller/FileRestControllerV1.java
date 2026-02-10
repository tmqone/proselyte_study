package com.tmq.module_25.controller;

import com.tmq.module_25.dto.FileDto;
import com.tmq.module_25.mapper.FileMapper;
import com.tmq.module_25.security.CustomPrincipal;
import com.tmq.module_25.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileRestControllerV1 {
    private final FileService fileService;
    private final FileMapper fileMapper;


    // USER CONTROLLERS

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<FileDto> uploadFileByUser(@RequestPart("file-data") FilePart filePart, Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return fileService
                .createFile(filePart, principal.getId())
                .map(fileMapper::map);
    }

    @GetMapping("/{id}")
    public Mono<FileDto> getFileInfoByUser(@PathVariable("id") Long fileId, Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return fileService
                .getFileInfo(fileId, principal.getId())
                .map(fileMapper::map);
    }


    @GetMapping
    public Flux<FileDto> getAllFilesInfoByUser(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

        return fileService.getAllFileInfoByUserId(principal.getId())
                .map(fileMapper::map);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteFileByUser(@PathVariable("id") Long fileId, Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return fileService.deleteFileByUser(fileId, principal.getId());
    }

    @GetMapping("/download/{id}")
    public Mono<ResponseEntity<Flux<DataBuffer>>> downloadFileByUser(@PathVariable("id") Long fileId, Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return fileService.downloadFile(fileId, principal.getId())
                .flatMap(entity -> {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setContentLength(entity.getMetaInfo().getContentLength());
                    httpHeaders.setContentType(MediaType.valueOf(entity.getMetaInfo().getContentType()));
                    return Mono.just(new ResponseEntity<>(
                            entity.getData(),
                            httpHeaders,
                            HttpStatus.OK
                    ));
                });

    }

    // MODERATOR / ADMIN CONTROLLERS

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @GetMapping("/admin/all")
    public Flux<FileDto> getAllFilesInfoByAdmin(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return fileService.getAll()
                .map(fileMapper::map);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @GetMapping("/admin/download/{id}")
    public Mono<ResponseEntity<Flux<DataBuffer>>> downloadFileByAdmin(@PathVariable("id") Long fileId, Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return fileService.downloadFileByAdmin(fileId, principal.getId())
                .flatMap(entity -> {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setContentLength(entity.getMetaInfo().getContentLength());
                    httpHeaders.setContentType(MediaType.valueOf(entity.getMetaInfo().getContentType()));
                    return Mono.just(new ResponseEntity<>(
                            entity.getData(),
                            httpHeaders,
                            HttpStatus.OK
                    ));
                });

    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @DeleteMapping("/admin/delete/{id}")
    public Mono<Void> deleteFileByAdmin(@PathVariable Long id, Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return fileService.deleteFileByAdmin(id, principal.getId());
    }
}
