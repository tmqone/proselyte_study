package com.tmq.repository;

import com.tmq.exception.FileNotFoundException;
import com.tmq.model.File;
import com.tmq.repository.hibernate.HibernateFileRepositoryImpl;
import com.tmq.util.FlywayUtil;
import com.tmq.util.HibernateUtil;
import com.tmq.util.InitUtil;
import com.tmq.util.PropertiesUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

@ExtendWith(InitUtil.class)
public class HibernateFileRepositoryImplTest {
    private static final HibernateFileRepositoryImpl fileRepository = HibernateFileRepositoryImpl.getInstance();

    @AfterEach
    public void resetQueryCounter() {
        HibernateUtil.getSessionFactory().getStatistics().clear();
    }

    @AfterAll
    public static void afterAll(){
        FlywayUtil.clean();
    }

    @Test
    public void findAll() {
        List<File> all = fileRepository.findAll();
        Assertions.assertNotNull(all);
        Assertions.assertFalse(all.isEmpty());
        all.forEach((file -> {
            Assertions.assertFalse(file.getIsDeleted());
        }));
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findAllByUserId_files_found() {
        List<File> allByUserId = fileRepository.findAllByUserId(1);
        Assertions.assertNotNull(allByUserId);
        allByUserId.forEach((file -> {
            Assertions.assertFalse(file.getIsDeleted());
        }));
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findAllByUserId_files_not_found() {
        List<File> allByUserId = fileRepository.findAllByUserId(0);
        Assertions.assertNotNull(allByUserId);
        Assertions.assertTrue(allByUserId.isEmpty());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findById_file_found() {
        int fileId = 1;
        Optional<File> byId = fileRepository.findById(fileId);
        Assertions.assertTrue(byId.isPresent());
        Assertions.assertFalse(byId.get().getIsDeleted());
        Assertions.assertEquals(fileId, byId.get().getId());
        Assertions.assertEquals("first_test", byId.get().getName());
        Assertions.assertEquals(System.getProperty("user.dir") + "/test_uploads/" + fileId + "/first_test",
                byId.get().getFilePath());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findById_file_not_found() {
        int fileId = 0;
        Optional<File> byId = fileRepository.findById(fileId);
        Assertions.assertFalse(byId.isPresent());
    }

    @Test
    public void findByUserId_files_found() {
        List<File> byUserId = fileRepository.findByUserId(1);
        byUserId.forEach((file -> {
            Assertions.assertFalse(file.getIsDeleted());
        }));
        Assertions.assertNotNull(byUserId);
        Assertions.assertFalse(byUserId.isEmpty());
        Assertions.assertEquals(2, byUserId.size());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void findByUserId_files_not_found() {
        List<File> byUserId = fileRepository.findByUserId(0);
        Assertions.assertNotNull(byUserId);
        Assertions.assertTrue(byUserId.isEmpty());
        Assertions.assertEquals(1, HibernateUtil.getSessionFactory().getStatistics().getQueryExecutionCount());
    }

    @Test
    public void save_success() {
        File saveSuccess = File.builder().name("save_success")
                .filePath(PropertiesUtil.get("file.path") + "/1/save_success.txt")
                .isDeleted(false)
                .build();
        Assertions.assertDoesNotThrow(() -> {
            File save = fileRepository.save(saveSuccess);
            Assertions.assertNotNull(save);
            Assertions.assertNotNull(save.getId());
        });
    }

    @Test
    public void update_file_found() {
        File updateSuccess = File.builder().name("update_success")
                .id(1)
                .filePath(PropertiesUtil.get("file.path") + "/1/update_success.txt")
                .isDeleted(false)
                .build();
        Assertions.assertDoesNotThrow(() -> {
            fileRepository.update(updateSuccess);
        });
        Optional<File> byId = fileRepository.findById(updateSuccess.getId());
        Assertions.assertTrue(byId.isPresent());
        Assertions.assertFalse(byId.get().getIsDeleted());
        Assertions.assertEquals(1, byId.get().getId());
        Assertions.assertEquals("update_success", byId.get().getName());
    }

    @Test
    public void update_file_not_found() {
        File updateSuccess = File.builder().name("update_success")
                .id(0)
                .filePath(PropertiesUtil.get("file.path") + "/1/update_success.txt")
                .isDeleted(false)
                .build();
        Assertions.assertThrows(FileNotFoundException.class, () -> {
            fileRepository.update(updateSuccess);
        });
    }

    @Test
    public void delete_file_found() {
        File saveSuccess = File.builder().name("save_success")
                .filePath(PropertiesUtil.get("file.path") + "/1/save_success.txt")
                .isDeleted(false)
                .build();

        Assertions.assertDoesNotThrow(() -> {
            File save = fileRepository.save(saveSuccess);
            Assertions.assertNotNull(save);
            Assertions.assertNotNull(save.getId());
            saveSuccess.setId(save.getId());
        });

        boolean delete = fileRepository.delete(saveSuccess.getId());
        Assertions.assertTrue(delete);
    }

    @Test
    public void delete_file_not_found() {
        boolean delete = fileRepository.delete(0);
        Assertions.assertFalse(delete);
    }
}
