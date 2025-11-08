package com.tmq.service;

import com.tmq.controller.api.v1.EventController;
import com.tmq.dto.event.*;
import com.tmq.exception.EventNotFoundException;
import com.tmq.exception.FileNotFoundException;
import com.tmq.exception.UserNotFoundException;
import com.tmq.mapper.EventMapper;
import com.tmq.model.Event;
import com.tmq.repository.EventRepository;
import com.tmq.repository.hibernate.HibernateEventRepositoryImpl;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventService {
    private static final EventService INSTANCE = new EventService();
    private static final EventRepository eventRepository = HibernateEventRepositoryImpl.getInstance();
    private final EventMapper eventMapper = new EventMapper();
    private static final FileService fileService = FileService.getInstance();
    private static final UserService userService = UserService.getInstance();

    public static EventService getInstance() {
        return INSTANCE;
    }

    public CreateEventResponse save(CreateEventRequest request) {
        if (!fileService.existsById(request.fileId())) throw new FileNotFoundException();
        if (!userService.existsById(request.userId())) throw new UserNotFoundException();
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

    public boolean existsById(Integer id) {
        return eventRepository.existsById(id);
    }
}
