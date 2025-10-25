package com.tmq.service;

import com.tmq.exception.GeneralException;
import com.tmq.exception.LabelNotFoundException;
import com.tmq.model.Label;
import com.tmq.repository.LabelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LabelServiceImplTest {
    @Mock
    LabelRepository labelRepository;

    @InjectMocks
    LabelServiceImpl labelService;

    @Test
    public void noConnectionToDatabase(){
        Mockito.when(labelRepository.findAll()).thenThrow(GeneralException.class);
        Assertions.assertThrows(GeneralException.class, () -> labelService.getAll());
    }

    @Test
    public void getAll_valuesInRepoExists() throws SQLException {
        List<Label> given = List.of(
                new Label(1L, "First"),
                new Label(2L, "Second"),
                new Label(3L, "Third")
        );

        List<Label> result;
        Mockito.when(labelRepository.findAll()).thenReturn(given);
        result = labelService.getAll();

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(given.get(0), result.get(0));
        Assertions.assertEquals(given.get(1), result.get(1));
        Assertions.assertEquals(given.get(2), result.get(2));
    }

    @Test
    public void getAll_valuesInRepoNotExists() throws SQLException {
        List<Label> given = Collections.emptyList();

        Mockito.when(labelRepository.findAll()).thenReturn(given);
        List<Label> result = labelService.getAll();

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void getById_valueInRepoExists() throws SQLException {
        Label given = new Label(1L, "First");

        Mockito.when(labelRepository.findById(1L)).thenReturn(Optional.of(given));
        Label result = labelService.getById(1L);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(given, result);
    }

    @Test
    public void getById_valueInRepoNotExists() throws SQLException {
        Mockito.when(labelRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(LabelNotFoundException.class, () -> labelService.getById(1L));
    }

    @Test
    public void getByName_valueInRepoExists() throws SQLException {
        Label given = new Label(1L, "First");

        Mockito.when(labelRepository.findByName(given.getName())).thenReturn(Optional.of(given));
        Label result = labelService.getByName(given.getName());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(given, result);
    }

    @Test
    public void getByName_valueInRepoNotExists() throws SQLException {
        Mockito.when(labelRepository.findByName("TestLabel")).thenReturn(Optional.empty());
        Assertions.assertThrows(LabelNotFoundException.class, () -> labelService.getByName("TestLabel"));
    }

    @Test
    public void update_valueInRepoExists() throws SQLException {
        Label given = new Label(3L, "Changed");
        Mockito.when(labelRepository.update(given)).thenReturn(given);
        Assertions.assertEquals(given, labelService.update(given));
    }

    @Test
    public void update_valueInRepoNotExists() throws SQLException {
        Label given = new Label(3L, "Changed");
        Mockito.when(labelRepository.update(given)).thenThrow(LabelNotFoundException.class);
        Assertions.assertThrows(LabelNotFoundException.class, () -> labelService.update(given));
    }

    @Test
    public void save() throws SQLException {
        Label given = new Label(null, "Saved");
        Mockito.when(labelRepository.save(given)).thenReturn(new Label(1L, "Saved"));
        Label result = labelService.save(given);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(given.getName(), result.getName());
        Assertions.assertNotNull(result.getId());
    }

    @Test
    public void delete_valueInRepoExists() throws SQLException {
        Label given = new Label(3L, "Deleted");
        Mockito.when(labelRepository.delete(given.getId())).thenReturn(true);
        Assertions.assertTrue(labelService.delete(given.getId()));
    }

    @Test
    public void delete_valueInRepoNotExists() throws SQLException {
        Label given = new Label(3L, "Deleted");
        Mockito.when(labelRepository.delete(given.getId())).thenThrow(LabelNotFoundException.class);
        Assertions.assertThrows(LabelNotFoundException.class, () -> labelService.delete(given.getId()));
    }
}
