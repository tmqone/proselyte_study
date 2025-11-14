package com.tmq.repository;

import com.tmq.exception.EventNotFoundException;
import com.tmq.model.Action;
import com.tmq.model.Event;
import com.tmq.model.File;
import com.tmq.model.User;
import com.tmq.repository.hibernate.HibernateEventRepositoryImpl;
import com.tmq.util.FlywayUtil;
import com.tmq.util.HibernateUtil;
import com.tmq.util.InitUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

@ExtendWith(InitUtil.class)
public class HibernateEventRepositoryImplTest {

    HibernateEventRepositoryImpl repository = HibernateEventRepositoryImpl.getInstance();

    @AfterEach
    public void resetQueryCounter() {
        HibernateUtil.getSessionFactory().getStatistics().clear();
    }

    @AfterAll
    public static void afterAll() {
        FlywayUtil.clean();
    }

    @Test
    public void findAll() {
        List<Event> all = HibernateUtil.handleRequest(() -> repository.findAll());
        Assertions.assertNotNull(all);
        Assertions.assertFalse(all.isEmpty());
        Assertions.assertEquals(6, all.size());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findAllByUser_success() {
        List<Event> allByUser = HibernateUtil.handleRequest(() -> repository.findAllByUser(1));
        Assertions.assertNotNull(allByUser);
        Assertions.assertFalse(allByUser.isEmpty());
        Assertions.assertEquals(6, allByUser.size());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findAllByUser_user_not_exists() {
        List<Event> allByUser = HibernateUtil.handleRequest(() -> repository.findAllByUser(0));
        Assertions.assertNotNull(allByUser);
        Assertions.assertTrue(allByUser.isEmpty());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findAllByUserWithAction_success() {
        List<Event> allByUserWithAction = HibernateUtil
                .handleRequest(() -> repository.findAllByUserWithAction(1, Action.GET));
        Assertions.assertNotNull(allByUserWithAction);
        Assertions.assertFalse(allByUserWithAction.isEmpty());
        Assertions.assertEquals(2, allByUserWithAction.size());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findAllByUserWithAction_user_not_exists() {
        List<Event> allByUserWithAction = HibernateUtil
                .handleRequest(() -> repository.findAllByUserWithAction(0, Action.GET));
        Assertions.assertNotNull(allByUserWithAction);
        Assertions.assertTrue(allByUserWithAction.isEmpty());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findAllByUserWithAction_not_exists() {
        List<Event> allByUserWithAction = HibernateUtil
                .handleRequest(() -> repository.findAllByUserWithAction(1, Action.DELETE));
        Assertions.assertNotNull(allByUserWithAction);
        Assertions.assertTrue(allByUserWithAction.isEmpty());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findAllByUserWithFileId() {
        List<Event> allByUserWithFileId = HibernateUtil
                .handleRequest(() -> repository.findAllByUserWithFileId(1, 1));
        Assertions.assertNotNull(allByUserWithFileId);
        Assertions.assertFalse(allByUserWithFileId.isEmpty());
        Assertions.assertEquals(3, allByUserWithFileId.size());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findAllByUserWithFileId_user_not_exists() {
        List<Event> allByUserWithFileId = HibernateUtil
                .handleRequest(() -> repository.findAllByUserWithFileId(0, 1));
        Assertions.assertNotNull(allByUserWithFileId);
        Assertions.assertTrue(allByUserWithFileId.isEmpty());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findAllByUserWithFileId_files_not_exists() {
        List<Event> allByUserWithFileId = HibernateUtil
                .handleRequest(() -> repository.findAllByUserWithFileId(1, 0));
        Assertions.assertNotNull(allByUserWithFileId);
        Assertions.assertTrue(allByUserWithFileId.isEmpty());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findByFileId_success() {
        List<Event> byFileId = HibernateUtil.handleRequest(() -> repository.findByFileId(1));
        Assertions.assertNotNull(byFileId);
        Assertions.assertFalse(byFileId.isEmpty());
        Assertions.assertEquals(3, byFileId.size());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findByFileId_files_not_exists() {
        List<Event> byFileId = HibernateUtil.handleRequest(() -> repository.findByFileId(0));
        Assertions.assertNotNull(byFileId);
        Assertions.assertTrue(byFileId.isEmpty());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findById_success() {
        Optional<Event> byId = HibernateUtil.handleRequest(() -> repository.findById(1));
        Assertions.assertNotNull(byId);
        Assertions.assertTrue(byId.isPresent());
        Assertions.assertEquals(1, byId.get().getId());
        Assertions.assertEquals(1, byId.get().getFile().getId());
        Assertions.assertEquals(Action.UPLOAD, byId.get().getAction());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findById_file_not_found() {
        Optional<Event> byId = HibernateUtil.handleRequest(() -> repository.findById(0));
        Assertions.assertNotNull(byId);
        Assertions.assertFalse(byId.isPresent());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void save_success() {
        Event build = Event.builder()
                .file(File.builder().id(1).build())
                .user(User.builder().id(1).build())
                .action(Action.UPLOAD).build();
        Event save = HibernateUtil.handleRequest(() -> repository.save(build));
        Assertions.assertNotNull(save);
        Assertions.assertNotNull(save.getId());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getEntityInsertCount());
    }

    @Test
    public void save_user_or_file_not_found() {
        Event build = Event.builder()
                .file(File.builder().id(0).build())
                .user(User.builder().id(0).build())
                .action(Action.UPLOAD).build();
        Assertions.assertThrows(ConstraintViolationException.class, () -> HibernateUtil.handleRequest(() -> repository.save(build)));
    }

    @Test
    public void update_success() {
        Event build = Event.builder()
                .file(File.builder().id(1).build())
                .user(User.builder().id(1).build())
                .action(Action.UPLOAD).build();
        Event save = HibernateUtil.handleRequest(() -> repository.save(build));

        save.setAction(Action.UPDATED);
        Event update = HibernateUtil.handleRequest(() -> repository.update(save));
        Assertions.assertNotNull(update);
        Assertions.assertEquals(Action.UPDATED, update.getAction());
    }

    @Test
    public void update_user_or_file_not_found() {
        Event build = Event.builder()
                .file(File.builder().id(1).build())
                .user(User.builder().id(1).build())
                .action(Action.UPLOAD).build();
        Event save = HibernateUtil.handleRequest(() -> repository.save(build));

        save.setUser(User.builder().id(0).build());
        save.setFile(File.builder().id(0).build());
        Assertions.assertThrows(ConstraintViolationException.class, () -> HibernateUtil
                .handleRequest(() -> repository.update(save)));
    }

    @Test
    public void delete_success() {
        Event build = Event.builder()
                .file(File.builder().id(1).build())
                .user(User.builder().id(1).build())
                .action(Action.UPLOAD).build();
        Event save = HibernateUtil.handleRequest(() -> repository.save(build));

        boolean delete = HibernateUtil.handleRequest(() -> repository.delete(save.getId()));
        Assertions.assertTrue(delete);
    }

    @Test
    public void delete_event_not_found() {
        boolean delete = HibernateUtil.handleRequest(() -> repository.delete(0));
        Assertions.assertFalse(delete);
    }
}
