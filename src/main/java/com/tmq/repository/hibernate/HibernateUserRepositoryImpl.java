package com.tmq.repository.hibernate;

import com.tmq.exception.UserNotFoundException;
import com.tmq.model.User;
import com.tmq.repository.UserRepository;
import com.tmq.util.HibernateUtil;
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
        Session session = HibernateUtil.getSession();
        return session.createQuery("""
                from User 
                """, User.class).list();
    }

    @Override
    public Optional<User> findById(Integer integer) {
        Session session = HibernateUtil.getSession();
        return Optional.ofNullable(session.createQuery("""
                from User u 
                where u.id = :id
                """, User.class).setParameter("id", integer).uniqueResult());
    }


    @Override
    public User save(User user) {
        Session session = HibernateUtil.getSession();
        session.persist(user);
        return user;
    }

    @Override
    public User update(User user) {
        Session session = HibernateUtil.getSession();
        User result = Optional.ofNullable(session.createQuery("""
                from User u 
                where u.id = :id
                """, User.class).setParameter("id", user.getId()).uniqueResult()).orElseThrow(UserNotFoundException::new);
        result.setUsername(user.getUsername());
        return result;
    }

    @Override
    public boolean delete(Integer integer) {
        Session session = HibernateUtil.getSession();
        int i = session.createQuery("""
                delete from User u
                where u.id = :id
                """).setParameter("id", integer).executeUpdate();
        return i > 0;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Session session = HibernateUtil.getSession();
        return Optional.ofNullable(session.createQuery("""
                from User u
                where u.username = :username
                """, User.class).setParameter("username", username).uniqueResult());
    }
}
