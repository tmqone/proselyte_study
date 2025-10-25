package com.tmq.service;

import com.tmq.exception.GeneralException;
import com.tmq.exception.WriterNotFoundException;
import com.tmq.model.Writer;
import com.tmq.repository.WriterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Test
    public void noConnectionToDatabase(){
        Mockito.when(writerRepository.findAll()).thenThrow(GeneralException.class);
        Assertions.assertThrows(GeneralException.class, () -> writerService.getAll());
    }

    @Test
    public void getAll_valuesInRepoExists() {
        List<Writer> given = List.of(
                new Writer(1L, "Artem", "Test", null),
                new Writer(2L, "Ivan", "Test", null),
                new Writer(3L, "Eugene", "Test", null)
        );

        List<Writer> result;
        Mockito.when(writerRepository.findAll()).thenReturn(given);
        result = writerService.getAll();

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(given.get(0), result.get(0));
        Assertions.assertEquals(given.get(1), result.get(1));
        Assertions.assertEquals(given.get(2), result.get(2));
    }

    @Test
    public void getAll_valuesInRepoNotExists()  {
        List<Writer> given = Collections.emptyList();

        Mockito.when(writerRepository.findAll()).thenReturn(given);
        List<Writer> result = writerService.getAll();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void getById_valueInRepoExists()  {
        Writer given = new Writer(1L, "Artem", "Test", null);

        Mockito.when(writerRepository.findById(1L)).thenReturn(Optional.of(given));
        Writer result = writerService.getById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(given, result);
    }

    @Test
    public void getById_valueInRepoNotExists()  {
        Mockito.when(writerRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(WriterNotFoundException.class, () -> writerService.getById(1L));
    }

    @Test
    public void getByName_valueInRepoExists(){
        Writer given = new Writer(1L, "Artem", "Test", null);
        Mockito.when(writerRepository
                .findByName(given.getFirstName(), given.getLastName()))
                .thenReturn(Optional.of(List.of(given)).orElseThrow(WriterNotFoundException::new));
        List<Writer> result = writerService.getByName(given.getFirstName(), given.getLastName());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(given, result.getFirst());
    }

    @Test
    public void getByName_valueInRepoNotExists(){
        Mockito.when(writerRepository
                .findByName("Artem", "Test"))
                .thenThrow(WriterNotFoundException.class);
        Assertions.assertThrows(WriterNotFoundException.class, () -> writerService.getByName("Artem", "Test"));
    }

    @Test
    public void update_valueInRepoExists()  {
        Writer given = new Writer(1L, "Artem", "Test", null);
        Mockito.when(writerRepository.update(given)).thenReturn(given);
        Assertions.assertEquals(given, writerService.update(given));
    }

    @Test
    public void update_valueInRepoNotExists()  {
        Writer given = new Writer(1L, "Artem", "Test", null);
        Mockito.when(writerRepository.update(given)).thenThrow(WriterNotFoundException.class);
        Assertions.assertThrows(WriterNotFoundException.class, () -> writerService.update(given));
    }

    @Test
    public void save()  {
        Writer given = new Writer(null, "Artem", "Test", null);
        Mockito.when(writerRepository.save(given))
                .thenReturn(new Writer(1L, "Artem", "Test", null));
        Writer result = writerService.save(given);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(given.getFirstName(), result.getFirstName());
        Assertions.assertEquals(given.getLastName(), result.getLastName());
        Assertions.assertNotNull(result.getId());
    }

    @Test
    public void delete_valueInRepoExists()  {
        Writer given = new Writer(1L, "Artem", "Test", null);
        Mockito.when(writerRepository.delete(given.getId())).thenReturn(true);
        Assertions.assertTrue(writerService.delete(given.getId()));
    }

    @Test
    public void delete_valueInRepoNotExists()  {
        Writer given = new Writer(1L, "Artem", "Test", null);
        Mockito.when(writerRepository.delete(given.getId())).thenThrow(WriterNotFoundException.class);
        Assertions.assertThrows(WriterNotFoundException.class, () -> writerService.delete(given.getId()));
    }

    @Test
    public void repositorySqlException()  {
        Writer given = new Writer(1L, "Artem", "Test", null);
        Mockito.when(writerRepository.save(given)).thenThrow(GeneralException.class);
        Assertions.assertThrows(GeneralException.class, () -> writerService.save(given));
    }
}
