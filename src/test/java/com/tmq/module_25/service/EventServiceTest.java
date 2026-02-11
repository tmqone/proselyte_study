package com.tmq.module_25.service;

import com.tmq.module_25.entity.EventEntity;
import com.tmq.module_25.entity.EventStatus;
import com.tmq.module_25.exception.EventNotFoundException;
import com.tmq.module_25.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void findAllByUserReturnsEvents() {
        EventEntity first = EventEntity.builder()
                .id(1L)
                .userId(1L)
                .fileId(1L)
                .status(EventStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        EventEntity second = EventEntity.builder()
                .id(2L)
                .userId(1L)
                .fileId(2L)
                .status(EventStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();

        when(eventRepository.findAllByUserId(1L)).thenReturn(Flux.just(first, second));

        StepVerifier.create(eventService.findAllByUser(1L))
                .expectNext(first, second)
                .verifyComplete();

        verify(eventRepository).findAllByUserId(1L);
    }

    @Test
    void findAllByUserEmptyReturnsError() {
        when(eventRepository.findAllByUserId(1L)).thenReturn(Flux.empty());

        StepVerifier.create(eventService.findAllByUser(1L))
                .expectError(EventNotFoundException.class)
                .verify();
    }

    @Test
    void findAllByFileIdByUserEmptyReturnsError() {
        when(eventRepository.findAllByUserIdAndFileId(1L, 1L)).thenReturn(Flux.empty());

        StepVerifier.create(eventService.findAllByFileIdByUser(1L, 1L))
                .expectError(EventNotFoundException.class)
                .verify();
    }

    @Test
    void findAllByAdminReturnsEvents() {
        EventEntity event = EventEntity.builder()
                .id(3L)
                .userId(2L)
                .fileId(1L)
                .status(EventStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();

        when(eventRepository.findAll()).thenReturn(Flux.just(event));

        StepVerifier.create(eventService.findAllByAdmin())
                .expectNext(event)
                .verifyComplete();

        verify(eventRepository).findAll();
    }

    @Test
    void createEventSavesEntity() {
        EventEntity input = EventEntity.builder()
                .userId(1L)
                .fileId(1L)
                .status(EventStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .build();
        EventEntity saved = EventEntity.builder()
                .id(10L)
                .userId(input.getUserId())
                .fileId(input.getFileId())
                .status(input.getStatus())
                .timestamp(input.getTimestamp())
                .build();

        when(eventRepository.save(input)).thenReturn(Mono.just(saved));

        StepVerifier.create(eventService.createEvent(input))
                .expectNext(saved)
                .verifyComplete();

        verify(eventRepository).save(input);
    }

    @Test
    void deleteEventCompletes() {
        when(eventRepository.deleteEvent(1L)).thenReturn(Mono.empty());

        StepVerifier.create(eventService.deleteEvent(1L))
                .verifyComplete();

        verify(eventRepository).deleteEvent(1L);
    }
}
