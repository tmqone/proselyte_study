package com.tmq.person.service.service;

import com.tmq.person.service.exception.CountryNotFoundException;
import com.tmq.person.service.model.CountryEntity;
import com.tmq.person.service.repository.CountryRepository;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    @Observed(name = "country.findByName")
    public CountryEntity findByName(String name) {
        return countryRepository.findByName(name)
                .orElseThrow(() -> new CountryNotFoundException("Country not found: " + name));
    }
}