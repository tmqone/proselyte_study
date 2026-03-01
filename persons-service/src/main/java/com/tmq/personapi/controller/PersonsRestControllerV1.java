package com.tmq.personapi.controller;

import com.tmq.common.api.person_api.PersonsApi;
import com.tmq.common.dto.IndividualDto;
import com.tmq.common.dto.IndividualPageDto;
import com.tmq.common.dto.IndividualWriteDto;
import com.tmq.common.dto.IndividualWriteResponseDto;
import com.tmq.personapi.service.PersonService;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/persons")
@RequiredArgsConstructor
public class PersonsRestControllerV1 implements PersonsApi {

    private final PersonService personService;

    @Override
    public ResponseEntity<Void> compensateRegistration(UUID id) {
        return null;
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        return null;
    }

    @Override
    public ResponseEntity<IndividualPageDto> findAllByEmail(@Nullable List<@Email String> email) {
        return null;
    }

    @Override
    public ResponseEntity<IndividualDto> findById(UUID id) {
        return null;
    }

    @Override
    public ResponseEntity<IndividualWriteResponseDto> registration(IndividualWriteDto individualWriteDto) {
        return null;
    }

    @Override
    public ResponseEntity<IndividualWriteResponseDto> update(UUID id, IndividualWriteDto individualWriteDto) {
        return null;
    }
}
