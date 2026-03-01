package com.tmq.personapi.service;

import com.tmq.personapi.exception.CountryNotFoundException;
import com.tmq.personapi.model.CountryEntity;
import com.tmq.personapi.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    public CountryEntity findByName(String name) {
        return countryRepository.findByName(name)
                .orElseThrow(() -> new CountryNotFoundException("Country not found: " + name));
    }
}