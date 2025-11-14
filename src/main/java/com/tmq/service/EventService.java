package com.tmq.service;

import com.tmq.dto.event.*;
import com.tmq.exception.EventNotFoundException;
import com.tmq.mapper.EventMapper;
import com.tmq.model.Action;
import com.tmq.model.Event;
import com.tmq.repository.EventRepository;
import com.tmq.repository.hibernate.HibernateEventRepositoryImpl;
import com.tmq.util.HibernateUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventService {
    private static final EventService INSTANCE = new EventService();
    private static final EventRepository eventRepository = HibernateEventRepositoryImpl.getInstance();
    private final EventMapper eventMapper = new EventMapper();

    public static EventService getInstance() {
        return INSTANCE;
    }

    public CreateEventResponse save(CreateEventRequest request) {
        return HibernateUtil.handleRequest(() -> {
            Event event = eventMapper.postToEntity(request);
            Event save = eventRepository.save(event);
            return eventMapper.postFromEntity(eventRepository.findById(save.getId()).orElseThrow(EventNotFoundException::new));
        });
    }

    public boolean insert(CreateEventRequest request) {
        return HibernateUtil.handleRequest(() -> {
            Event event = eventMapper.postToEntity(request);
            eventRepository.save(event);
            return true;
        });
    }

    public boolean insert(List<CreateEventRequest> request) {
        return HibernateUtil.handleRequest(() -> {
            List<Event> events = request.stream().map(eventMapper::postToEntity).toList();
            eventRepository.save(events);
            return true;
        });
    }

    public UpdateEventResponse update(UpdateEventRequest request) {
        return HibernateUtil.handleRequest(() -> {
            Event event = eventMapper.updateToEntity(request);
            Event update = eventRepository.update(event);
            return eventMapper.updateFromEntity(update);
        });
    }

    public boolean delete(Integer id) {
        return HibernateUtil.handleRequest(() -> {
            if (eventRepository.delete(id)) return true;
            throw new EventNotFoundException();
        });
    }

    public List<FindAllEventResponse> findAll(){
        return HibernateUtil.handleRequest(() -> eventMapper.findAllFromEntity(eventRepository.findAll()));
    }

    public FindEventByIdResponse findById(Integer id) {
        return HibernateUtil.handleRequest(() -> eventMapper
                .getByIdFromEntity(eventRepository.findById(id).orElseThrow(EventNotFoundException::new)));
    }

    public FindEventByIdResponse findByIdByUser(Integer id, Integer userId) {
        return HibernateUtil.handleRequest(() -> eventMapper.getByIdFromEntity(eventRepository
                .findById(id)
                .filter(event -> event.getUser().getId().equals(userId))
                .orElseThrow(EventNotFoundException::new)));
    }

    public List<FindAllEventResponse> findAllByUserId(Integer userId) {
        return HibernateUtil.handleRequest(() -> eventMapper.findAllFromEntity(eventRepository.findAllByUser(userId)));
    }

    public List<FindAllEventResponse> findAllByUserIdWithAction(Integer userId, Action action) {
        return HibernateUtil.handleRequest(() -> eventMapper
                .findAllFromEntity(eventRepository.findAllByUserWithAction(userId, action)));
    }

    public List<FindAllEventResponse> findAllByUserIdWithFileId(Integer userId, Integer fileId) {
        return HibernateUtil.handleRequest(() -> eventMapper
                .findAllFromEntity(eventRepository.findAllByUserWithFileId(userId, fileId)
                .stream()
                .toList()));
    }

    public List<FindAllEventResponse> findByFileId(Integer fileId){
        return HibernateUtil.handleRequest(() -> eventMapper.findAllFromEntity(eventRepository.findByFileId(fileId)));
    }

}
