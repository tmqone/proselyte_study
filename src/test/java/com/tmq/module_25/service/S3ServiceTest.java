package com.tmq.module_25.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.async.ResponsePublisher;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @Mock
    private S3AsyncClient s3AsyncClient;

    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        s3Service = new S3Service(s3AsyncClient);
        ReflectionTestUtils.setField(s3Service, "bucket", "test-bucket");
    }

    @Test
    void uploadFileUsesBucketAndReturnsKey() {
        FilePart filePart = mock(FilePart.class);
        when(filePart.filename()).thenReturn("doc.txt");
        when(filePart.transferTo(any(Path.class))).thenReturn(Mono.empty());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        when(filePart.headers()).thenReturn(headers);

        ArgumentCaptor<PutObjectRequest> requestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        when(s3AsyncClient.putObject(requestCaptor.capture(), any(AsyncRequestBody.class)))
                .thenReturn(CompletableFuture.completedFuture(PutObjectResponse.builder().build()));

        StepVerifier.create(s3Service.uploadFile(filePart))
                .assertNext(key -> {
                    PutObjectRequest request = requestCaptor.getValue();
                    assertThat(request.bucket()).isEqualTo("test-bucket");
                    assertThat(request.key()).isEqualTo(key);
                    assertThat(request.contentType()).isEqualTo(MediaType.TEXT_PLAIN_VALUE);
                    assertThat(request.contentLength()).isEqualTo(0L);
                })
                .verifyComplete();
    }
}
