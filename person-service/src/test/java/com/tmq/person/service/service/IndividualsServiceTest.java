package com.tmq.person.service.service;

import com.tmq.person.service.exception.UserNotFoundException;
import com.tmq.person.service.metrics.IndividualsMetrics;
import com.tmq.person.service.model.AddressEntity;
import com.tmq.person.service.model.CountryEntity;
import com.tmq.person.service.model.IndividualsEntity;
import com.tmq.person.service.model.UserEntity;
import com.tmq.person.service.repository.IndividualsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.tmq.person.service.metrics.IndividualsMetrics.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndividualsServiceTest {

    @Mock
    private IndividualsRepository individualsRepository;

    @Mock
    private CountryService countryService;

    @Mock
    private IndividualsMetrics individualsMetrics;

    @InjectMocks
    private IndividualsService individualsService;
    
    @Test
    void findById_whenExists_returnsEntity() {
        UUID id = UUID.randomUUID();
        IndividualsEntity entity = new IndividualsEntity();
        entity.setId(id);

        when(individualsRepository.findById(id)).thenReturn(Optional.of(entity));

        IndividualsEntity result = individualsService.findById(id);

        assertThat(result).isEqualTo(entity);
        verify(individualsMetrics).recordSuccess(OP_FIND_BY_ID);
        verify(individualsMetrics, never()).recordNotFound();
    }

    @Test
    void findById_whenNotFound_throwsUserNotFoundException() {
        UUID id = UUID.randomUUID();

        when(individualsRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> individualsService.findById(id))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(id.toString());

        verify(individualsMetrics).recordNotFound();
        verify(individualsMetrics, never()).recordSuccess(OP_FIND_BY_ID);
    }
    
    @Test
    void findAllByEmails_whenSuccess_returnsEntities() {
        List<String> emails = List.of("a@test.com", "b@test.com");
        List<IndividualsEntity> entities = List.of(new IndividualsEntity(), new IndividualsEntity());

        when(individualsRepository.findAllByEmails(emails)).thenReturn(entities);

        List<IndividualsEntity> result = individualsService.findAllByEmails(emails);

        assertThat(result).hasSize(2).isEqualTo(entities);
        verify(individualsMetrics).recordSuccess(OP_FIND_ALL_BY_EMAILS);
    }

    @Test
    void findAllByEmails_whenRepositoryThrows_recordsErrorAndRethrows() {
        List<String> emails = List.of("a@test.com");

        when(individualsRepository.findAllByEmails(emails)).thenThrow(new RuntimeException("db error"));

        assertThatThrownBy(() -> individualsService.findAllByEmails(emails))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("db error");

        verify(individualsMetrics).recordError(OP_FIND_ALL_BY_EMAILS);
        verify(individualsMetrics, never()).recordSuccess(OP_FIND_ALL_BY_EMAILS);
    }
    
    @Test
    void create_whenSuccess_savesAndRecordsMetric() {
        CountryEntity country = new CountryEntity();
        country.setName("Germany");

        CountryEntity resolvedCountry = new CountryEntity();
        resolvedCountry.setId(1);
        resolvedCountry.setName("Germany");

        AddressEntity address = new AddressEntity();
        address.setCountry(country);

        UserEntity user = new UserEntity();
        user.setAddress(address);

        IndividualsEntity entity = new IndividualsEntity();
        entity.setUser(user);

        IndividualsEntity saved = new IndividualsEntity();
        saved.setId(UUID.randomUUID());
        saved.setUser(user);

        when(countryService.findByName("Germany")).thenReturn(resolvedCountry);
        when(individualsRepository.save(entity)).thenReturn(saved);

        IndividualsEntity result = individualsService.create(entity);

        assertThat(result).isEqualTo(saved);
        assertThat(address.getCountry()).isEqualTo(resolvedCountry);
        verify(individualsMetrics).recordSuccess(OP_CREATE);
    }

    @Test
    void create_whenRepositoryThrows_recordsErrorAndRethrows() {
        CountryEntity country = new CountryEntity();
        country.setName("Germany");

        AddressEntity address = new AddressEntity();
        address.setCountry(country);

        UserEntity user = new UserEntity();
        user.setAddress(address);

        IndividualsEntity entity = new IndividualsEntity();
        entity.setUser(user);

        when(countryService.findByName("Germany")).thenReturn(country);
        when(individualsRepository.save(entity)).thenThrow(new RuntimeException("save failed"));

        assertThatThrownBy(() -> individualsService.create(entity))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("save failed");

        verify(individualsMetrics).recordError(OP_CREATE);
        verify(individualsMetrics, never()).recordSuccess(OP_CREATE);
    }

    @Test
    void update_whenEntityExists_savesAndRecordsMetric() {
        UUID id = UUID.randomUUID();

        CountryEntity country = new CountryEntity();
        country.setName("France");

        CountryEntity resolvedCountry = new CountryEntity();
        resolvedCountry.setId(2);
        resolvedCountry.setName("France");

        AddressEntity address = new AddressEntity();
        address.setCountry(country);

        UserEntity user = new UserEntity();
        user.setAddress(address);

        IndividualsEntity entity = new IndividualsEntity();
        entity.setId(id);
        entity.setUser(user);

        IndividualsEntity existing = new IndividualsEntity();
        existing.setId(id);

        when(individualsRepository.findById(id)).thenReturn(Optional.of(existing));
        when(countryService.findByName("France")).thenReturn(resolvedCountry);
        when(individualsRepository.save(entity)).thenReturn(entity);

        IndividualsEntity result = individualsService.update(entity);

        assertThat(result).isEqualTo(entity);
        assertThat(address.getCountry()).isEqualTo(resolvedCountry);
        verify(individualsMetrics).recordSuccess(OP_UPDATE);
    }

    @Test
    void update_whenEntityNotFound_throwsUserNotFoundException() {
        UUID id = UUID.randomUUID();

        CountryEntity country = new CountryEntity();
        country.setName("France");

        AddressEntity address = new AddressEntity();
        address.setCountry(country);

        UserEntity user = new UserEntity();
        user.setAddress(address);

        IndividualsEntity entity = new IndividualsEntity();
        entity.setId(id);
        entity.setUser(user);

        when(individualsRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> individualsService.update(entity))
                .isInstanceOf(UserNotFoundException.class);

        verify(individualsMetrics, never()).recordSuccess(OP_UPDATE);
        verify(individualsMetrics).recordError(OP_UPDATE);
    }
    
    @Test
    void softDelete_whenSuccess_recordsSuccessMetric() {
        UUID id = UUID.randomUUID();

        doNothing().when(individualsRepository).softDelete(id);

        individualsService.softDelete(id);

        verify(individualsRepository).softDelete(id);
        verify(individualsMetrics).recordSuccess(OP_SOFT_DELETE);
    }

    @Test
    void softDelete_whenRepositoryThrows_recordsErrorAndRethrows() {
        UUID id = UUID.randomUUID();

        doThrow(new RuntimeException("db error")).when(individualsRepository).softDelete(id);

        assertThatThrownBy(() -> individualsService.softDelete(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("db error");

        verify(individualsMetrics).recordError(OP_SOFT_DELETE);
        verify(individualsMetrics, never()).recordSuccess(OP_SOFT_DELETE);
    }

    @Test
    void delete_whenSuccess_recordsSuccessMetric() {
        UUID id = UUID.randomUUID();

        doNothing().when(individualsRepository).deleteById(id);

        individualsService.delete(id);

        verify(individualsRepository).deleteById(id);
        verify(individualsMetrics).recordSuccess(OP_DELETE);
    }

    @Test
    void delete_whenRepositoryThrows_recordsErrorAndRethrows() {
        UUID id = UUID.randomUUID();

        doThrow(new RuntimeException("constraint violation")).when(individualsRepository).deleteById(id);

        assertThatThrownBy(() -> individualsService.delete(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("constraint violation");

        verify(individualsMetrics).recordError(OP_DELETE);
        verify(individualsMetrics, never()).recordSuccess(OP_DELETE);
    }
}