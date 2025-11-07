package com.tmq.service;

import com.tmq.dto.event.*;
import com.tmq.exception.EventNotFoundException;
import com.tmq.mapper.EventMapper;
import com.tmq.model.Event;
import com.tmq.repository.EventRepository;
import com.tmq.repository.hibernate.HibernateEventRepositoryImpl;

import java.util.List;

public class EventService {
    private final EventRepository eventRepository = new HibernateEventRepositoryImpl();
    private final EventMapper eventMapper = new EventMapper();

    public CreateEventResponse save(CreateEventRequest request) {
        Event event = eventMapper.postToEntity(request);
        Event save = eventRepository.save(event);
        return eventMapper.postFromEntity(save);
    }

    public UpdateEventResponse update(UpdateEventRequest request) {
        Event event = eventMapper.updateToEntity(request);
        Event update = eventRepository.update(event);
        return eventMapper.updateFromEntity(update);
    }

    public boolean delete(DeleteEventRequest request) {
        return eventRepository.delete(request.id());
    }

    public FindEventByIdResponse findById(Integer id) {
        return eventMapper.getByIdFromEntity(eventRepository.findById(id).orElseThrow(EventNotFoundException::new));
    }

    public List<FindAllEventResponse> findAll() {
        return eventMapper.findAllFromEntity(eventRepository.findAll());
    }
}
