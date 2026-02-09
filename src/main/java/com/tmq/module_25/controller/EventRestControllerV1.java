package com.tmq.module_25.controller;

import com.tmq.module_25.dto.EventDto;
import com.tmq.module_25.mapper.EventMapper;
import com.tmq.module_25.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventRestControllerV1 {
    private final EventService eventService;
    private final EventMapper eventMapper;

    @GetMapping
    public Flux<EventDto> findAll(){
        return eventService.findAll().map(eventMapper::map);
    }
}
