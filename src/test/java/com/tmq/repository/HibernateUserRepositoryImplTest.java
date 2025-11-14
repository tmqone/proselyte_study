package com.tmq.repository;

import com.tmq.exception.UserNotFoundException;
import com.tmq.model.Role;
import com.tmq.model.User;
import com.tmq.repository.hibernate.HibernateUserRepositoryImpl;
import com.tmq.util.FlywayUtil;
import com.tmq.util.HibernateUtil;
import com.tmq.util.InitUtil;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

@ExtendWith(InitUtil.class)
public class HibernateUserRepositoryImplTest {

    private static final HibernateUserRepositoryImpl userRepository = HibernateUserRepositoryImpl.getInstance();

    @AfterAll
    public static void afterAll(){
        FlywayUtil.clean();
    }

    @AfterEach
    public void verifyQueryCount() {
        Assertions.assertEquals(0, HibernateUtil.getSessionFactory().getStatistics().getCollectionFetchCount());
    }

    @AfterEach
    public void resetQueryCounter() {
        HibernateUtil.getSessionFactory().getStatistics().clear();
    }

    @Test
    public void findAll() {
        List<User> list = HibernateUtil.handleRequest(userRepository::findAll);
        Assertions.assertFalse(list.isEmpty());
        Assertions.assertEquals("user", list.get(0).getUsername());
        Assertions.assertEquals(1, HibernateUtil.getSession().getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findById_success() {
        User user = HibernateUtil.handleRequest(() -> userRepository.findById(1).orElseThrow(AssertionError::new));
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getId());
        Assertions.assertNotNull(user.getUsername());
        Assertions.assertNotNull(user.getPassword());
        Assertions.assertNotNull(user.getRole());

        Assertions.assertEquals("user", user.getUsername());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findById_notFound() {
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            HibernateUtil.handleRequest(() -> userRepository.findById(0).orElseThrow(UserNotFoundException::new));
        });
    }

    @Test
    public void save_success() {
        User user = User.builder().username("tmq").role(Role.USER).password("123".toCharArray()).build();
        Assertions.assertDoesNotThrow(() -> {
            HibernateUtil.handleRequest(() -> userRepository.save(user));
        });
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getFlushCount());

    }

    @Test
    public void save_username_exists() {
        User user = User.builder().username("user").role(Role.USER).password("123".toCharArray()).build();
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            HibernateUtil.handleRequest(() -> userRepository.save(user));
        });
    }

    @Test
    public void update_success() {
        User user = User.builder().username("update_success").role(Role.USER).password("123".toCharArray()).build();
        User save = HibernateUtil.handleRequest(() -> userRepository.save(user));
        save.setUsername("update_success");
        HibernateUtil.handleRequest(() -> userRepository.update(user));
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals("update_success", user.getUsername());
        Assertions.assertEquals(2, HibernateUtil.getSessionFactory().getStatistics().getFlushCount());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getEntityLoadCount());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getEntityInsertCount());
    }

    @Test
    public void update_username_exists() {
        User user = User.builder().username("update_username_exists").role(Role.USER).password("123".toCharArray()).build();
        User save = HibernateUtil.handleRequest(() -> userRepository.save(user));
        save.setUsername("user");
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            HibernateUtil.handleRequest(() -> userRepository.update(user));
        });
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getFlushCount());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getEntityLoadCount());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getEntityInsertCount());
    }

    @Test
    public void update_user_not_found() {
        User user = User.builder().id(0).build();
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            HibernateUtil.handleRequest(() -> userRepository.update(user));
        });
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void delete_success() {
        User user = User.builder().username("delete_success").role(Role.USER).password("123".toCharArray()).build();
        User save = HibernateUtil.handleRequest(() -> userRepository.save(user));
        boolean delete = HibernateUtil.handleRequest(() -> userRepository.delete(save.getId()));
        Assertions.assertTrue(delete);
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getFlushCount());
    }

    @Test
    public void delete_user_not_found() {
        boolean delete = HibernateUtil.handleRequest(() -> userRepository.delete(0));
        Assertions.assertFalse(delete);
    }

    @Test
    public void findByUsername_success() {
        Optional<User> user = HibernateUtil.handleRequest(() -> userRepository.findByUsername("user"));
        Assertions.assertDoesNotThrow(() -> {
            user.get();
        });
        Assertions.assertEquals("user", user.get().getUsername());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findByUsername_user_not_found() {
        Optional<User> user = HibernateUtil.handleRequest(() -> userRepository.findByUsername("null"));
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            user.orElseThrow(UserNotFoundException::new);
        });
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }
}
