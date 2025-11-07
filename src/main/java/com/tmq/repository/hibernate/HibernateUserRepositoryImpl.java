package com.tmq.repository.hibernate;

import com.tmq.exception.UserNotFoundException;
import com.tmq.model.User;
import com.tmq.repository.UserRepository;
import com.tmq.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class HibernateUserRepositoryImpl implements UserRepository {
    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    @Override
    public Optional<User> findById(Integer integer) {
        try (Session session = HibernateUtil.getSession()) {
            return Optional.ofNullable(session.find(User.class, integer));
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
            User merge = findById(user.getId()).orElseThrow(UserNotFoundException::new);
            merge.setUsername(user.getUsername());
            merge.setEvents(user.getEvents());
            session.getTransaction().commit();
            return merge;
        }
    }

    @Override
    public boolean delete(Integer integer) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.remove(User.builder().id(integer).build());
            session.getTransaction().commit();
        }
        return true;
    }
}
