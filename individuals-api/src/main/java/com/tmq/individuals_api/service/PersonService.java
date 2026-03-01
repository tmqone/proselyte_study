package com.tmq.individuals_api.service;

import com.tmq.common.api.person_api.PersonsApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonsApiClient personsApiClient;
}
