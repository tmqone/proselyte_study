package com.tmq.person.service.controller;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class PostgresContainerBase {

    @Container
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    static {
        postgres.start();
    }

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",                postgres::getJdbcUrl);
        registry.add("spring.datasource.username",           postgres::getUsername);
        registry.add("spring.datasource.password",           postgres::getPassword);
        registry.add("spring.datasource.hikari.schema",      () -> "person");
        registry.add("spring.flyway.url",                    postgres::getJdbcUrl);
        registry.add("spring.flyway.user",                   postgres::getUsername);
        registry.add("spring.flyway.password",               postgres::getPassword);
        registry.add("spring.flyway.default-schema",         () -> "person");
        registry.add("spring.flyway.create-schemas",         () -> "true");
        registry.add("spring.flyway.baseline-on-migrate",    () -> "true");
    }
}