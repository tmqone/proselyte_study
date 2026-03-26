package com.tmq.individuals.config;

import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class TraceIdResponseFilter implements WebFilter {

    private final Tracer tracer;

    public TraceIdResponseFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange)
                .doFirst(() -> {
                    TraceContext context = tracer.currentTraceContext().context();
                    if (context != null) {
                        exchange.getResponse().getHeaders()
                                .add("X-Trace-Id", context.traceId());
                    }
                });
    }
}
