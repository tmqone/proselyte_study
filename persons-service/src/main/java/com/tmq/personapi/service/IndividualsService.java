package com.tmq.personapi.service;

import com.tmq.personapi.exception.UserNotFoundException;
import com.tmq.personapi.model.AddressEntity;
import com.tmq.personapi.model.IndividualsEntity;
import com.tmq.personapi.repository.IndividualsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class IndividualsService {
    public final IndividualsRepository individualsRepository;
    private final CountryService countryService;

    @Transactional
    public void softDelete(UUID id) {
        individualsRepository.softDelete(id);
    }

    @Transactional
    public void delete(UUID id) {
        individualsRepository.deleteById(id);
    }

    public IndividualsEntity findById(UUID id) {
        return individualsRepository.findById(id)
                .map(individualsEntity -> {
                    log.info("Found individual with id {} = {}", id, individualsEntity);
                    return individualsEntity;
                })
                .orElseThrow(() -> {
                    log.error("User not found with id {}", id);
                    return new UserNotFoundException("User with id " + id + " not found");
                });
    }

    public List<IndividualsEntity> findAllByEmails(List<String> emails) {
        log.info("Founding all individuals by email {}", emails);
        return individualsRepository.findAllByEmails(emails);
    }

    @Transactional
    public IndividualsEntity create(IndividualsEntity individualsEntity) {
        AddressEntity address = individualsEntity.getUser().getAddress();
        address.setCountry(countryService.findByName(address.getCountry().getName()));
        log.info("Creating individuals {}", individualsEntity);
        return individualsRepository.save(individualsEntity);
    }

    @Transactional
    public IndividualsEntity update(IndividualsEntity individualsEntity) {
        IndividualsEntity byId = findById(individualsEntity.getId());
        AddressEntity address = individualsEntity.getUser().getAddress();
        address.setCountry(countryService.findByName(address.getCountry().getName()));
        log.info("Updating individuals {}", individualsEntity);
        return individualsRepository.save(individualsEntity);
    }
}
