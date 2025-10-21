package com.tmq.service;

import com.tmq.exception.GeneralException;
import com.tmq.exception.WriterNotFoundException;
import com.tmq.model.Writer;
import com.tmq.repository.WriterRepository;
import com.tmq.util.DatabaseUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class WriterServiceImplTest {
    @Mock
    WriterRepository writerRepository;

    @InjectMocks
    WriterServiceImpl writerService;

    MockedStatic<DatabaseUtil> databaseUtil;
    Connection connection;

    @BeforeEach
    public void setMocked() {
        databaseUtil = Mockito.mockStatic(DatabaseUtil.class);
        connection = Mockito.mock(Connection.class);
        databaseUtil.when(DatabaseUtil::getConnection).thenReturn(connection);
    }

    @AfterEach
    public void mockedInvocation() throws SQLException {
        Mockito.verify(connection).close();
        connection.close();
        databaseUtil.close();
        connection = null;
        databaseUtil = null;
    }

    @Test
    public void noConnectionToDatabase() throws SQLException {
        Mockito.when(writerRepository.findAll(connection)).thenThrow(SQLException.class);
        Assertions.assertThrows(GeneralException.class, () -> writerService.getAll());
    }

    @Test
    public void getAll_valuesInRepoExists() throws SQLException {
        List<Writer> given = List.of(
                new Writer(1L, "Artem", "Test", null),
                new Writer(2L, "Ivan", "Test", null),
                new Writer(3L, "Eugene", "Test", null)
        );

        List<Writer> result;
        Mockito.when(writerRepository.findAll(connection)).thenReturn(given);
        result = writerService.getAll();

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(given.get(0), result.get(0));
        Assertions.assertEquals(given.get(1), result.get(1));
        Assertions.assertEquals(given.get(2), result.get(2));
    }

    @Test
    public void getAll_valuesInRepoNotExists() throws SQLException {
        List<Writer> given = Collections.emptyList();

        Mockito.when(writerRepository.findAll(connection)).thenReturn(given);
        List<Writer> result = writerService.getAll();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void getById_valueInRepoExists() throws SQLException {
        Writer given = new Writer(1L, "Artem", "Test", null);

        Mockito.when(writerRepository.findById(1L, connection)).thenReturn(Optional.of(given));
        Writer result = writerService.getById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(given, result);
    }

    @Test
    public void getById_valueInRepoNotExists() throws SQLException {
        Mockito.when(writerRepository.findById(1L, connection)).thenReturn(Optional.empty());
        Assertions.assertThrows(WriterNotFoundException.class, () -> writerService.getById(1L));
    }

    @Test
    public void getByName_valueInRepoExists(){
        Writer given = new Writer(1L, "Artem", "Test", null);
        Mockito.when(writerRepository
                .findByName(given.getFirstName(), given.getLastName(), connection))
                .thenReturn(Optional.of(List.of(given)).orElseThrow(WriterNotFoundException::new));
        List<Writer> result = writerService.getByName(given.getFirstName(), given.getLastName());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(given, result.getFirst());
    }

    @Test
    public void getByName_valueInRepoNotExists(){
        Mockito.when(writerRepository
                .findByName("Artem", "Test", connection))
                .thenThrow(WriterNotFoundException.class);
        Assertions.assertThrows(WriterNotFoundException.class, () -> writerService.getByName("Artem", "Test"));
    }

    @Test
    public void update_valueInRepoExists() throws SQLException {
        Writer given = new Writer(1L, "Artem", "Test", null);
        Mockito.when(writerRepository.update(given, connection)).thenReturn(given);
        Assertions.assertEquals(given, writerService.update(given));
    }

    @Test
    public void update_valueInRepoNotExists() throws SQLException {
        Writer given = new Writer(1L, "Artem", "Test", null);
        Mockito.when(writerRepository.update(given, connection)).thenThrow(WriterNotFoundException.class);
        Assertions.assertThrows(WriterNotFoundException.class, () -> writerService.update(given));
    }

    @Test
    public void save() throws SQLException {
        Writer given = new Writer(null, "Artem", "Test", null);
        Mockito.when(writerRepository.save(given, connection))
                .thenReturn(new Writer(1L, "Artem", "Test", null));
        Writer result = writerService.save(given);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(given.getFirstName(), result.getFirstName());
        Assertions.assertEquals(given.getLastName(), result.getLastName());
        Assertions.assertNotNull(result.getId());
    }

    @Test
    public void delete_valueInRepoExists() throws SQLException {
        Writer given = new Writer(1L, "Artem", "Test", null);
        Mockito.when(writerRepository.delete(given.getId(), connection)).thenReturn(true);
        Assertions.assertTrue(writerService.delete(given.getId()));
    }

    @Test
    public void delete_valueInRepoNotExists() throws SQLException {
        Writer given = new Writer(1L, "Artem", "Test", null);
        Mockito.when(writerRepository.delete(given.getId(), connection)).thenThrow(WriterNotFoundException.class);
        Assertions.assertThrows(WriterNotFoundException.class, () -> writerService.delete(given.getId()));
    }

    @Test
    public void repositorySqlException() throws SQLException {
        Writer given = new Writer(1L, "Artem", "Test", null);
        Mockito.when(writerRepository.save(given, connection)).thenThrow(SQLException.class);
        Assertions.assertThrows(GeneralException.class, () -> writerService.save(given));
    }
}
