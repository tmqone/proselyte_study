package com.tmq.repository.hibernate;

import com.tmq.exception.EventNotFoundException;
import com.tmq.model.Action;
import com.tmq.model.Event;
import com.tmq.repository.EventRepository;
import com.tmq.util.HibernateUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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
        Session session = HibernateUtil.getSession();
        return session.createQuery("""
                from Event e
                left join fetch e.file
                left join fetch e.user
                """, Event.class).list();
    }

    @Override
    public List<Event> findAllByUser(Integer userId) {
        Session session = HibernateUtil.getSession();
        return session.createQuery("""
                from Event e
                left join fetch e.file
                left join fetch e.user
                where e.user.id = :userId
                """, Event.class).setParameter("userId", userId).list();
    }

    @Override
    public List<Event> findAllByUserWithAction(Integer userId, Action action) {
        Session session = HibernateUtil.getSession();
        return session.createQuery("""
                        from Event e
                        left join fetch e.file
                        left join fetch e.user
                        where e.user.id = :userId and e.action = :action
                        """, Event.class).setParameter("userId", userId)
                .setParameter("action", action).list();
    }

    @Override
    public List<Event> findAllByUserWithFileId(Integer userId, Integer fileId) {
        Session session = HibernateUtil.getSession();
        return session.createQuery("""
                        from Event e
                        left join fetch e.file
                        left join fetch e.user
                        where e.user.id = :userId and e.file.id = :fileId
                        """, Event.class).setParameter("userId", userId)
                .setParameter("fileId", fileId).list();
    }

    @Override
    public List<Event> findByFileId(Integer fileId) {
        Session session = HibernateUtil.getSession();
        return session.createQuery("""
                        from Event e
                        left join fetch e.file
                        left join fetch e.user
                        where e.file.id = :fileId
                        """, Event.class)
                .setParameter("fileId", fileId).list();
    }

    @Override
    public Optional<Event> findById(Integer integer) {
        Session session = HibernateUtil.getSession();
        return Optional.ofNullable(session.createQuery("""
                        from Event e
                        left join fetch e.file
                        left join fetch e.user
                        where e.id = :id
                        """, Event.class)
                .setParameter("id", integer)
                .uniqueResult());
    }

    @Override
    public Event save(Event event) {
        Session session = HibernateUtil.getSession();
        session.persist(event);
        return event;
    }

    @Override
    public Event update(Event event) {
        Session session = HibernateUtil.getSession();

        int i = session.createQuery("""
                        update Event e 
                        set e.file = :file, e.user = :user, e.action = :action
                        where e.id = :id
                        """)
                .setParameter("id", event.getId())
                .setParameter("file", event.getFile())
                .setParameter("user", event.getUser())
                .setParameter("action", event.getAction())
                .executeUpdate();

        if (i < 1) throw new EventNotFoundException();

        return Optional.ofNullable(
                session.createQuery("""
                                from Event e
                                left join fetch e.file
                                left join fetch e.user
                                where e.id = :id
                                """, Event.class)
                        .setParameter("id", event.getId())
                        .uniqueResult()
        ).orElseThrow(EventNotFoundException::new);
    }

    @Override
    public boolean delete(Integer integer) {
        Session session = HibernateUtil.getSession();
        int i = session.createQuery("delete Event e where e.id = :id")
                .setParameter("id", integer)
                .executeUpdate();
        return i > 0;
    }

    @Override
    public List<Event> save(List<Event> events) {
        Session session = HibernateUtil.getSession();
        events.forEach(session::persist);
        return events;
    }
}
