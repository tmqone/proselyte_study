package com.tmq.individuals_api.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KeycloakMetrics {

    public static final String OP_GET_USER_TOKEN  = "get_user_token";
    public static final String OP_GET_ADMIN_TOKEN = "get_admin_token";
    public static final String OP_REFRESH_TOKEN   = "refresh_token";
    public static final String OP_CREATE_USER     = "create_user";

    private static final String METRIC_REQUESTS = "keycloak.requests.total";
    private static final String METRIC_DURATION = "keycloak.request.duration";

    private final MeterRegistry registry;

    private final Map<String, Counter> successCounters;
    private final Map<String, Counter> errorCounters;
    private final Map<String, Timer>   timers;

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

        this.timers = Map.of(
                OP_GET_USER_TOKEN,  timer(OP_GET_USER_TOKEN),
                OP_GET_ADMIN_TOKEN, timer(OP_GET_ADMIN_TOKEN),
                OP_REFRESH_TOKEN,   timer(OP_REFRESH_TOKEN),
                OP_CREATE_USER,     timer(OP_CREATE_USER)
        );
    }

    public Timer.Sample startSample() {
        return Timer.start(registry);
    }

    public void stopTimer(Timer.Sample sample, String operation) {
        sample.stop(timers.get(operation));
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

    private Timer timer(String operation) {
        return Timer.builder(METRIC_DURATION)
                .description("Duration of Keycloak HTTP requests")
                .tag("operation", operation)
                .register(registry);
    }
}