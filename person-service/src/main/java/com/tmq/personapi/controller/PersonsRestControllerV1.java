package com.tmq.personapi.controller;

import com.tmq.common.api.person_api.PersonsApi;
import com.tmq.common.dto.IndividualDto;
import com.tmq.common.dto.IndividualPageDto;
import com.tmq.common.dto.IndividualWriteDto;
import com.tmq.common.dto.IndividualWriteResponseDto;
import com.tmq.personapi.mapper.IndividualsMapper;
import com.tmq.personapi.model.IndividualsEntity;
import com.tmq.personapi.service.IndividualsService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PersonsRestControllerV1 implements PersonsApi {

    private final IndividualsService individualsService;
    private final IndividualsMapper individualsMapper;

    @Override
    public ResponseEntity<Void> compensateRegistration(@PathVariable UUID id) {
        individualsService.delete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        individualsService.softDelete(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<IndividualPageDto> findAllByEmail(@Nullable @RequestParam List<String> email) {
        List<IndividualsEntity> allByEmails = individualsService.findAllByEmails(email);
        if (CollectionUtils.isEmpty(allByEmails)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .body(individualsMapper.toPageDto(allByEmails));
    }

    @Override
    public ResponseEntity<IndividualDto> findById(@PathVariable UUID id) {
        return ResponseEntity.ok()
                .body(individualsMapper.toDto(individualsService.findById(id)));
    }

    @Override
    public ResponseEntity<IndividualWriteResponseDto> registration(@RequestBody IndividualWriteDto individualWriteDto) {
        IndividualsEntity entity = individualsService.create(individualsMapper.toEntity(individualWriteDto));
        return ResponseEntity.status(HttpStatusCode.valueOf(201))
                .body(individualsMapper.toWriteResponseDto(entity));
    }

    @Override
    public ResponseEntity<IndividualWriteResponseDto> update(@PathVariable UUID id, @RequestBody IndividualWriteDto individualWriteDto) {
        IndividualsEntity individualsEntity = individualsMapper.toEntity(individualWriteDto);
        individualsEntity.setId(id);
        IndividualsEntity entity = individualsService.update(individualsEntity);
        return ResponseEntity.status(HttpStatusCode.valueOf(201))
                .body(individualsMapper.toWriteResponseDto(entity));
    }
}
