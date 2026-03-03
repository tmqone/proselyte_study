package com.tmq.individuals_api.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KeycloakMetrics {

    public static final String OP_GET_USER_TOKEN  = "get_user_token";
    public static final String OP_GET_ADMIN_TOKEN = "get_admin_token";
    public static final String OP_REFRESH_TOKEN   = "refresh_token";
    public static final String OP_CREATE_USER     = "create_user";

    private static final String METRIC_REQUESTS = "keycloak.requests.total";

    private final MeterRegistry registry;

    private final Map<String, Counter> successCounters;
    private final Map<String, Counter> errorCounters;

    public KeycloakMetrics(MeterRegistry registry) {
        this.registry = registry;

        this.successCounters = Map.of(
                OP_GET_USER_TOKEN,  counter(OP_GET_USER_TOKEN,  "success"),
                OP_GET_ADMIN_TOKEN, counter(OP_GET_ADMIN_TOKEN, "success"),
                OP_REFRESH_TOKEN,   counter(OP_REFRESH_TOKEN,   "success"),
                OP_CREATE_USER,     counter(OP_CREATE_USER,     "success")
        );

        this.errorCounters = Map.of(
                OP_GET_USER_TOKEN,  counter(OP_GET_USER_TOKEN,  "error"),
                OP_GET_ADMIN_TOKEN, counter(OP_GET_ADMIN_TOKEN, "error"),
                OP_REFRESH_TOKEN,   counter(OP_REFRESH_TOKEN,   "error"),
                OP_CREATE_USER,     counter(OP_CREATE_USER,     "error")
        );
    }

    public void recordSuccess(String operation) {
        successCounters.get(operation).increment();
    }

    public void recordError(String operation) {
        errorCounters.get(operation).increment();
    }
    
    private Counter counter(String operation, String status) {
        return Counter.builder(METRIC_REQUESTS)
                .description("Total number of Keycloak HTTP requests")
                .tag("operation", operation)
                .tag("status", status)
                .register(registry);
    }
}