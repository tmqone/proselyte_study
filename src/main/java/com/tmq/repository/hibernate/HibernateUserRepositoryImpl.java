package com.tmq.repository.hibernate;

import com.tmq.exception.UserNotFoundException;
import com.tmq.model.User;
import com.tmq.repository.UserRepository;
import com.tmq.util.HibernateUtil;
import jakarta.persistence.LockModeType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HibernateUserRepositoryImpl implements UserRepository {

    private static final HibernateUserRepositoryImpl INSTANCE = new HibernateUserRepositoryImpl();

    public static HibernateUserRepositoryImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("""
            from User u
            """, User.class).list();
        }
    }

    @Override
    public Optional<User> findById(Integer integer) {
        try (Session session = HibernateUtil.getSession()) {
            return Optional.ofNullable(session.createQuery("""
            from User u 
            where u.id = :id
            """, User.class).setParameter("id", integer).uniqueResult());
        }
    }

    @Override
    public User save(User user) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
            return user;
        }
    }

    @Override
    public User update(User user) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            User result = Optional.ofNullable(session.createQuery("""
            from User u 
            where u.id = :id
            """, User.class).setParameter("id", user.getId()).uniqueResult()).orElseThrow(UserNotFoundException::new);
            result.setUsername(user.getUsername());
            session.getTransaction().commit();
            return result;
        }
    }

    @Override
    public boolean delete(Integer integer) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            User user = Optional.ofNullable(session
                    .createQuery("from User u where u.id = :id", User.class)
                    .setParameter("id", integer)
                    .uniqueResult()
            ).orElseThrow(UserNotFoundException::new);
            session.remove(user);
            session.getTransaction().commit();
        }
        return true;
    }

    @Override
    public boolean existsById(Integer integer) {
        try (Session session = HibernateUtil.getSession()){
            return session.createQuery("select count(f) from User f where f.id = :id", Long.class)
                    .setParameter("id", integer).uniqueResult() > 0;
        }
    }
}
