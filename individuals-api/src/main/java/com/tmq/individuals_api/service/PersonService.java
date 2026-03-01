package com.tmq.individuals_api.service;

import com.tmq.common.api.person_api.PersonsApiClient;
import com.tmq.common.dto.IndividualWriteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonsApiClient personsApiClient;

    public Mono<String> register(IndividualWriteDto dto) {
        return Mono.just(Objects.requireNonNull(personsApiClient.registration(dto).getBody()).getId());
    }

    public Mono<Void> compensateRegistration(UUID uuid) {
        personsApiClient.compensateRegistration(uuid);
        return Mono.empty();
    }
}
