package com.tmq.repository.hibernate;

import com.tmq.exception.EventNotFoundException;
import com.tmq.model.Event;
import com.tmq.repository.EventRepository;
import com.tmq.util.HibernateUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateEventRepositoryImpl implements EventRepository {
    private static final HibernateEventRepositoryImpl INSTANCE = new HibernateEventRepositoryImpl();

    public static HibernateEventRepositoryImpl getInstance() {
        return INSTANCE;
    }

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
            session.getTransaction().commit();
            return findById(event.getId()).orElseThrow(EventNotFoundException::new);
        }
    }

    @Override
    public Event update(Event event) {
        try (Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            Event result = session.find(Event.class, event.getId());
            result.setUser(event.getUser());
            result.setAction(event.getAction());
            result.setFile(event.getFile());
            session.getTransaction().commit();
            return findById(result.getId()).orElseThrow(EventNotFoundException::new);
        }
    }

    @Override
    public boolean delete(Integer integer) {
        try (Session session = HibernateUtil.getSession()){
            session.beginTransaction();
            session.remove(Event.builder().id(integer).build());
            session.getTransaction().commit();
            return true;
        }
    }

    public boolean existsById(Integer integer) {
        try (Session session = HibernateUtil.getSession()){
            return session.createQuery("select count(f) from Event f where f.id = :id", Long.class)
                    .setParameter("id", integer).uniqueResult() > 0;
        }
    }
}
