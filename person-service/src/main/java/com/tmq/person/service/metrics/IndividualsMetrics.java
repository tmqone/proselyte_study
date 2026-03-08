package com.tmq.person.service.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class IndividualsMetrics {

    public static final String OP_CREATE              = "create";
    public static final String OP_UPDATE              = "update";
    public static final String OP_SOFT_DELETE         = "soft_delete";
    public static final String OP_DELETE              = "delete";
    public static final String OP_FIND_BY_ID          = "find_by_id";
    public static final String OP_FIND_ALL_BY_EMAILS  = "find_all_by_emails";

    private static final String METRIC = "individuals.operations.total";

    private final Map<String, Counter> successCounters;
    private final Map<String, Counter> errorCounters;
    private final Counter findByIdNotFound;

    public IndividualsMetrics(MeterRegistry registry) {
        this.successCounters = Map.of(
                OP_CREATE,             counter(registry, OP_CREATE,             "success"),
                OP_UPDATE,             counter(registry, OP_UPDATE,             "success"),
                OP_SOFT_DELETE,        counter(registry, OP_SOFT_DELETE,        "success"),
                OP_DELETE,             counter(registry, OP_DELETE,             "success"),
                OP_FIND_BY_ID,         counter(registry, OP_FIND_BY_ID,         "success"),
                OP_FIND_ALL_BY_EMAILS, counter(registry, OP_FIND_ALL_BY_EMAILS, "success")
        );
        this.errorCounters = Map.of(
                OP_CREATE,             counter(registry, OP_CREATE,             "error"),
                OP_UPDATE,             counter(registry, OP_UPDATE,             "error"),
                OP_SOFT_DELETE,        counter(registry, OP_SOFT_DELETE,        "error"),
                OP_DELETE,             counter(registry, OP_DELETE,             "error"),
                OP_FIND_BY_ID,         counter(registry, OP_FIND_BY_ID,         "error"),
                OP_FIND_ALL_BY_EMAILS, counter(registry, OP_FIND_ALL_BY_EMAILS, "error")
        );
        this.findByIdNotFound = counter(registry, OP_FIND_BY_ID, "not_found");
    }

    public void recordSuccess(String operation) {
        successCounters.get(operation).increment();
    }

    public void recordError(String operation) {
        errorCounters.get(operation).increment();
    }

    public void recordNotFound() {
        findByIdNotFound.increment();
    }

    private Counter counter(MeterRegistry registry, String operation, String status) {
        return Counter.builder(METRIC)
                .description("Total number of individuals service operations")
                .tag("operation", operation)
                .tag("status", status)
                .register(registry);
    }
}