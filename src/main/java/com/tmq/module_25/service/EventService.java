package com.tmq.module_25.service;

import com.tmq.module_25.entity.EventEntity;
import com.tmq.module_25.exception.EventNotFoundException;
import com.tmq.module_25.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {
    private final EventRepository eventRepository;

    public Flux<EventEntity> findAllByUser(Long userId){
        return eventRepository.findAllByUserId(userId)
                .switchIfEmpty(Mono.error(EventNotFoundException::new));
    }

    public Flux<EventEntity> findAllByFileIdByUser(Long userId, Long fileId){
        return eventRepository.findAllByUserIdAndFileId(userId, fileId)
                .switchIfEmpty(Mono.error(EventNotFoundException::new));
    }

    public Flux<EventEntity> findAllByAdmin(){
        return eventRepository.findAll()
                .switchIfEmpty(Mono.error(EventNotFoundException::new));
    }

    public Flux<EventEntity> findAllByFileIdByAdmin(Long fileId){
        return eventRepository.findAllByFileId(fileId)
                .switchIfEmpty(Mono.error(EventNotFoundException::new));
    }

    @Transactional
    public Mono<EventEntity> createEvent(EventEntity event) {
        return eventRepository.save(event);
    }

    @Transactional
    public Mono<Void> deleteEvent(Long id){
        return eventRepository.deleteEvent(id);
    }
}
