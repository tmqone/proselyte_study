package com.tmq.module_25.service;

import com.tmq.module_25.entity.EventEntity;
import com.tmq.module_25.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public Flux<EventEntity> findAll(){
        return eventRepository.findAll();
    }

    public Mono<EventEntity> createEvent(EventEntity event) {
        return eventRepository.save(event);
    }

    public Mono<Void> deleteEvent(Long id){
        return eventRepository.deleteEvent(id);
    }
}
