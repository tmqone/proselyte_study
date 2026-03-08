package com.tmq.individuals_api.service;

import com.tmq.person.api.PersonsApiClient;
import com.tmq.person.dto.IndividualWriteDto;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.observability.micrometer.Micrometer;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonsApiClient personsApiClient;
    private final ObservationRegistry observationRegistry;

    public Mono<String> register(IndividualWriteDto dto) {
        return Mono.fromCallable(() ->
                        Objects.requireNonNull(personsApiClient.registration(dto).getBody()).getId())
                .subscribeOn(Schedulers.boundedElastic())
                .name("person.register")
                .tap(Micrometer.observation(observationRegistry));
    }

    public Mono<Void> compensateRegistration(UUID uuid) {
        return Mono.fromRunnable(() -> personsApiClient.compensateRegistration(uuid))
                .subscribeOn(Schedulers.boundedElastic())
                .then()
                .name("person.compensateRegistration")
                .tap(Micrometer.observation(observationRegistry));
    }
}
