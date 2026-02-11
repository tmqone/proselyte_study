package com.tmq.module_25.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class S3Config {
    @Value("${aws.url}")
    private String url;
    @Value("${aws.port}")
    private Integer port;
    @Value("${aws.accessKey}")
    private String accessKey;
    @Value("${aws.secretKey}")
    private String secretKey;

    @Bean
    S3AsyncClient s3AsyncClient(){
        return S3AsyncClient.builder()
                .endpointOverride(URI.create(url + ":" + port))
                .region(Region.EU_CENTRAL_1)
                .credentialsProvider(credentialsProvider())
                .serviceConfiguration(builder ->
                        builder.pathStyleAccessEnabled(true))
                .build();
    }

    @Bean
    S3Client s3syncClient(){
        return S3Client.builder()
                .endpointOverride(URI.create(url + ":" + port))
                .region(Region.EU_CENTRAL_1)
                .credentialsProvider(credentialsProvider())
                .serviceConfiguration(builder ->
                        builder.pathStyleAccessEnabled(true))
                .build();
    }

    @Bean
    StaticCredentialsProvider credentialsProvider() {
        return StaticCredentialsProvider
                .create(AwsBasicCredentials.create(accessKey, secretKey));
    }
}
