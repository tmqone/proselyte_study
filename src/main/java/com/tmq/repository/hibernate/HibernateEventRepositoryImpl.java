package com.tmq.repository.hibernate;

import com.tmq.exception.EventNotFoundException;
import com.tmq.model.Event;
import com.tmq.repository.EventRepository;
import com.tmq.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class HibernateEventRepositoryImpl implements EventRepository {
    @Override
    public List<Event> findAll() {
        try (Session session = HibernateUtil.getSession()){
            return session.createQuery("from Event", Event.class).list();
        }
    }

    @Override
    public Optional<Event> findById(Integer integer) {
        try (Session session = HibernateUtil.getSession()){
            return Optional.ofNullable(session.get(Event.class, integer));
        }
    }

    @Override
    public Event save(Event event) {
        try (Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            session.persist(event);
            session.find(Event.class, event.getId());
            session.getTransaction().commit();
            return event;
        }
    }

    @Override
    public Event update(Event event) {
        try (Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            Event merge = findById(event.getId()).orElseThrow(EventNotFoundException::new);
            merge.setFile(event.getFile());
            merge.setUser(event.getUser());
            merge.setAction(event.getAction());
            session.getTransaction().commit();
            return merge;
        }
    }

    @Override
    public boolean delete(Integer integer) {
        try (Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            session.remove(integer);
            session.getTransaction().commit();
            return true;
        }
    }
}
