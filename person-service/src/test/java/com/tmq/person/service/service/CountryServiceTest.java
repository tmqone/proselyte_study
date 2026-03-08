package com.tmq.person.service.service;

import com.tmq.person.service.exception.CountryNotFoundException;
import com.tmq.person.service.model.CountryEntity;
import com.tmq.person.service.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    @Test
    void findByName_whenCountryExists_returnsCountryEntity() {
        CountryEntity country = new CountryEntity();
        country.setName("Germany");

        when(countryRepository.findByName("Germany")).thenReturn(Optional.of(country));

        CountryEntity result = countryService.findByName("Germany");

        assertThat(result).isEqualTo(country);
        verify(countryRepository).findByName("Germany");
    }

    @Test
    void findByName_whenCountryNotFound_throwsCountryNotFoundException() {
        when(countryRepository.findByName("Unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> countryService.findByName("Unknown"))
                .isInstanceOf(CountryNotFoundException.class)
                .hasMessageContaining("Unknown");

        verify(countryRepository).findByName("Unknown");
    }
}