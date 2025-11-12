package com.tmq.repository;

import com.tmq.model.Action;
import com.tmq.model.Event;

import java.util.List;

public interface EventRepository extends GenericRepository<Integer, Event> {
    List<Event> findAllByUser(Integer userId);

    List<Event> findAllByUserWithAction(Integer userId, Action action);

    List<Event> findAllByUserWithFileId(Integer userId, Integer fileId);

    List<Event> findByFileId(Integer fileId);

    List<Event> save(List<Event> events);
}
