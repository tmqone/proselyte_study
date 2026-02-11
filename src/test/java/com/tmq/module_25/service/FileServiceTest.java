package com.tmq.module_25.service;

import com.tmq.module_25.entity.EventEntity;
import com.tmq.module_25.entity.EventStatus;
import com.tmq.module_25.entity.FileDownloadEntity;
import com.tmq.module_25.entity.FileEntity;
import com.tmq.module_25.entity.FileMetaInfo;
import com.tmq.module_25.entity.FileStatus;
import com.tmq.module_25.exception.FileNotFoundException;
import com.tmq.module_25.repository.FileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private S3Service s3Service;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private EventService eventService;

    @InjectMocks
    private FileService fileService;

    @Test
    void createFileUploadsSavesAndCreatesEvent() {
        FilePart filePart = org.mockito.Mockito.mock(FilePart.class);
        when(filePart.filename()).thenReturn("report.txt");

        FileEntity saved = new FileEntity(10L, "report.txt", "loc-1", FileStatus.ACTIVE);
        when(s3Service.uploadFile(filePart)).thenReturn(Mono.just("loc-1"));
        when(fileRepository.save(any(FileEntity.class))).thenReturn(Mono.just(saved));
        when(eventService.createEvent(any(EventEntity.class)))
                .thenReturn(Mono.just(EventEntity.builder().id(100L).build()));

        StepVerifier.create(fileService.createFile(filePart, 7L))
                .expectNext(saved)
                .verifyComplete();

        ArgumentCaptor<FileEntity> fileCaptor = ArgumentCaptor.forClass(FileEntity.class);
        verify(fileRepository).save(fileCaptor.capture());
        FileEntity toSave = fileCaptor.getValue();
        assertThat(toSave.getName()).isEqualTo("report.txt");
        assertThat(toSave.getLocation()).isEqualTo("loc-1");
        assertThat(toSave.getStatus()).isEqualTo(FileStatus.ACTIVE);

        ArgumentCaptor<EventEntity> eventCaptor = ArgumentCaptor.forClass(EventEntity.class);
        verify(eventService).createEvent(eventCaptor.capture());
        EventEntity event = eventCaptor.getValue();
        assertThat(event.getFileId()).isEqualTo(10L);
        assertThat(event.getUserId()).isEqualTo(7L);
        assertThat(event.getStatus()).isEqualTo(EventStatus.CREATED);
    }

    @Test
    void downloadFileReturnsFileDownloadEntity() {
        FileEntity file = new FileEntity(1L, "1.pdf", "loc-1", FileStatus.ACTIVE);
        FileDownloadEntity download = FileDownloadEntity.builder()
                .metaInfo(FileMetaInfo.builder().contentType("text/plain").contentLength(5L).build())
                .data(Flux.empty())
                .build();

        when(fileRepository.findFileWithUserId(1L, 1L)).thenReturn(Mono.just(file));
        when(s3Service.downloadFile("loc-1")).thenReturn(Mono.just(download));

        StepVerifier.create(fileService.downloadFile(1L, 1L))
                .expectNext(download)
                .verifyComplete();

        verify(s3Service).downloadFile("loc-1");
    }

    @Test
    void downloadFileWhenNotFoundErrors() {
        when(fileRepository.findFileWithUserId(1L, 1L)).thenReturn(Mono.empty());

        StepVerifier.create(fileService.downloadFile(1L, 1L))
                .expectError(FileNotFoundException.class)
                .verify();
    }

    @Test
    void downloadFileByAdminWhenNotFoundErrors() {
        when(fileRepository.findFileEntityById(2L)).thenReturn(Mono.empty());

        StepVerifier.create(fileService.downloadFileByAdmin(2L, 99L))
                .expectError(FileNotFoundException.class)
                .verify();
    }

    @Test
    void getFileInfoWhenNotFoundErrors() {
        when(fileRepository.findFileWithUserId(3L, 3L)).thenReturn(Mono.empty());

        StepVerifier.create(fileService.getFileInfo(3L, 3L))
                .expectError(FileNotFoundException.class)
                .verify();
    }

    @Test
    void getAllFileInfoByUserIdReturnsRepositoryValues() {
        FileEntity first = new FileEntity(1L, "1.pdf", "loc-1", FileStatus.ACTIVE);
        FileEntity second = new FileEntity(2L, "2.png", "loc-2", FileStatus.ACTIVE);
        when(fileRepository.findAllByUserId(1L)).thenReturn(Flux.just(first, second));

        StepVerifier.create(fileService.getAllFileInfoByUserId(1L))
                .expectNext(first, second)
                .verifyComplete();
    }

    @Test
    void getAllReturnsRepositoryValues() {
        List<FileEntity> files = List.of(
                new FileEntity(1L, "1.pdf", "loc-1", FileStatus.ACTIVE),
                new FileEntity(2L, "2.png", "loc-2", FileStatus.ACTIVE)
        );
        when(fileRepository.findAll()).thenReturn(Flux.fromIterable(files));

        StepVerifier.create(fileService.getAll())
                .expectNext(files.get(0), files.get(1))
                .verifyComplete();
    }

    @Test
    void deleteFileByUserCompletesAndCreatesEvent() {
        FileEntity file = new FileEntity(1L, "1.pdf", "loc-1", FileStatus.ACTIVE);
        when(fileRepository.findFileWithUserId(1L, 1L)).thenReturn(Mono.just(file));
        when(fileRepository.deleteById(1L)).thenReturn(Mono.empty());
        when(eventService.createEvent(any(EventEntity.class)))
                .thenReturn(Mono.just(EventEntity.builder().id(100L).build()));

        StepVerifier.create(fileService.deleteFileByUser(1L, 1L))
                .verifyComplete();

        ArgumentCaptor<EventEntity> eventCaptor = ArgumentCaptor.forClass(EventEntity.class);
        verify(eventService).createEvent(eventCaptor.capture());
        EventEntity event = eventCaptor.getValue();
        assertThat(event.getFileId()).isEqualTo(1L);
        assertThat(event.getUserId()).isEqualTo(1L);
        assertThat(event.getStatus()).isEqualTo(EventStatus.DELETED);
    }

    @Test
    void deleteFileByUserWhenNotFoundErrors() {
        when(fileRepository.findFileWithUserId(5L, 5L)).thenReturn(Mono.empty());

        StepVerifier.create(fileService.deleteFileByUser(5L, 5L))
                .expectError(FileNotFoundException.class)
                .verify();
    }

    @Test
    void deleteFileByAdminCompletes() {
        when(fileRepository.deleteById(9L)).thenReturn(Mono.empty());

        StepVerifier.create(fileService.deleteFileByAdmin(9L, 2L))
                .verifyComplete();

        verify(fileRepository).deleteById(9L);
    }
}
