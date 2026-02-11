package com.tmq.module_25.repository;

import com.tmq.module_25.entity.EventStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EventRepositoryTest extends RepositoryConfigurationTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    void findAllByUserId() {
        StepVerifier.create(eventRepository.findAllByUserId(1L).collectList())
                .assertNext(events -> {
                    assertThat(events).hasSize(3);
                    assertThat(events).allMatch(event -> event.getUserId().equals(1L));
                })
                .verifyComplete();
    }

    @Test
    void findAllByUserIdAndFileId() {
        StepVerifier.create(eventRepository.findAllByUserIdAndFileId(1L, 1L).collectList())
                .assertNext(events -> {
                    assertThat(events).hasSize(1);
                    assertThat(events).allMatch(event -> event.getFileId().equals(1L));
                })
                .verifyComplete();
    }

    @Test
    void findAllByFileId() {
        StepVerifier.create(eventRepository.findAllByFileId(1L).collectList())
                .assertNext(events -> {
                    assertThat(events).hasSize(2);
                    assertThat(events).allMatch(event -> event.getFileId().equals(1L));
                })
                .verifyComplete();
    }

    @Test
    void deleteEventUpdatesStatus() {
        StepVerifier.create(eventRepository.deleteEvent(1L).then(eventRepository.findById(1L)))
                .assertNext(event -> {
                    assertThat(event.getId()).isEqualTo(1L);
                    assertThat(event.getStatus()).isEqualTo(EventStatus.DELETED);
                })
                .verifyComplete();
    }
}
