package com.tmq.person.service.service;

import com.tmq.person.service.exception.UserNotFoundException;
import com.tmq.person.service.metrics.IndividualsMetrics;
import com.tmq.person.service.model.AddressEntity;
import com.tmq.person.service.model.IndividualsEntity;
import com.tmq.person.service.repository.IndividualsRepository;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.tmq.person.service.metrics.IndividualsMetrics.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class IndividualsService {
    public final IndividualsRepository individualsRepository;
    private final CountryService countryService;
    private final IndividualsMetrics individualsMetrics;

    @Observed(name = "individuals.softDelete")
    @Transactional
    public void softDelete(UUID id) {
        try {
            individualsRepository.softDelete(id);
            individualsMetrics.recordSuccess(OP_SOFT_DELETE);
        } catch (Exception e) {
            individualsMetrics.recordError(OP_SOFT_DELETE);
            throw e;
        }
    }

    @Observed(name = "individuals.delete")
    @Transactional
    public void delete(UUID id) {
        try {
            individualsRepository.deleteById(id);
            individualsMetrics.recordSuccess(OP_DELETE);
        } catch (Exception e) {
            individualsMetrics.recordError(OP_DELETE);
            throw e;
        }
    }

    @Observed(name = "individuals.findById")
    public IndividualsEntity findById(UUID id) {
        return individualsRepository.findById(id)
                .map(individualsEntity -> {
                    log.info("Found individual with id {} = {}", id, individualsEntity);
                    individualsMetrics.recordSuccess(OP_FIND_BY_ID);
                    return individualsEntity;
                })
                .orElseThrow(() -> {
                    log.error("User not found with id {}", id);
                    individualsMetrics.recordNotFound();
                    return new UserNotFoundException("User with id " + id + " not found");
                });
    }

    @Observed(name = "individuals.findAllByEmails")
    public List<IndividualsEntity> findAllByEmails(List<String> emails) {
        try {
            log.info("Founding all individuals by email {}", emails);
            List<IndividualsEntity> result = individualsRepository.findAllByEmails(emails);
            individualsMetrics.recordSuccess(OP_FIND_ALL_BY_EMAILS);
            return result;
        } catch (Exception e) {
            individualsMetrics.recordError(OP_FIND_ALL_BY_EMAILS);
            throw e;
        }
    }

    @Observed(name = "individuals.create")
    @Transactional
    public IndividualsEntity create(IndividualsEntity individualsEntity) {
        try {
            AddressEntity address = individualsEntity.getUser().getAddress();
            address.setCountry(countryService.findByName(address.getCountry().getName()));
            log.info("Creating individuals {}", individualsEntity);
            IndividualsEntity saved = individualsRepository.save(individualsEntity);
            individualsMetrics.recordSuccess(OP_CREATE);
            return saved;
        } catch (Exception e) {
            individualsMetrics.recordError(OP_CREATE);
            throw e;
        }
    }

    @Observed(name = "individuals.update")
    @Transactional
    public IndividualsEntity update(IndividualsEntity individualsEntity) {
        try {
            IndividualsEntity byId = findById(individualsEntity.getId());
            AddressEntity address = individualsEntity.getUser().getAddress();
            address.setCountry(countryService.findByName(address.getCountry().getName()));
            log.info("Updating individuals {}", individualsEntity);
            IndividualsEntity saved = individualsRepository.save(individualsEntity);
            individualsMetrics.recordSuccess(OP_UPDATE);
            return saved;
        } catch (Exception e) {
            individualsMetrics.recordError(OP_UPDATE);
            throw e;
        }
    }
}
