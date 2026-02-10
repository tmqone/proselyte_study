package com.tmq.module_25.controller;

import com.tmq.module_25.dto.EventDto;
import com.tmq.module_25.dto.EventRequestDto;
import com.tmq.module_25.dto.FileDto;
import com.tmq.module_25.entity.EventEntity;
import com.tmq.module_25.mapper.EventMapper;
import com.tmq.module_25.security.CustomPrincipal;
import com.tmq.module_25.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
@Slf4j
public class EventRestControllerV1 {
    private final EventService eventService;
    private final EventMapper eventMapper;


    // USER CONTROLLERS
    @GetMapping
    public Flux<EventDto> findAllByUser(Authentication authentication){
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return eventService.findAllByUser(principal.getId())
                .map(eventMapper::map);
    }

    @GetMapping(params = "file_id")
    public Flux<EventDto> findByFileIdByUser(@RequestParam("file_id") Long fileId, Authentication authentication){
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return eventService.findAllByFileIdByUser(principal.getId(), fileId)
                .map(eventMapper::map);
    }


    // ADMIN / MODERATOR CONTROLLERS
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @GetMapping("/admin")
    public Flux<EventDto> findAllByAdmin(Authentication authentication){
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return eventService.findAllByAdmin()
                .map(eventMapper::map);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @PostMapping(value = "/admin")
    public Mono<EventDto> create(@RequestBody EventRequestDto dto, Authentication authentication){
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return eventService.createEvent(EventEntity.builder()
                        .fileId(dto.getFileId())
                        .userId(dto.getUserId())
                        .status(dto.getStatus())
                        .timestamp(LocalDateTime.now())
                        .build())
                .map(eventMapper::map);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @PutMapping(value = "/admin")
    public Mono<EventDto> findByFileIdByAdmin(@RequestBody EventDto dto, Authentication authentication){
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return eventService.createEvent(EventEntity.builder()
                .id(dto.getId())
                .fileId(dto.getFileId())
                .status(dto.getStatus())
                .timestamp(dto.getTimestamp())
                .userId(dto.getUserId())
                .build())
                .map(eventMapper::map);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @DeleteMapping(value = "/admin/{id}")
    public Mono<Void> deleteByAdmin(@PathVariable("id") Long eventId, Authentication authentication){
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return eventService.deleteEvent(eventId);
    }
}
