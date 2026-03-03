package com.tmq.personapi.mapper;

import com.tmq.personapi.model.CountryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    default String toCountryCode(CountryEntity entity) {
        return entity != null ? entity.getName() : null;
    }

    default CountryEntity toEntity(String countryCode) {
        if (countryCode == null) return null;
        CountryEntity entity = new CountryEntity();
        entity.setName(countryCode);
        return entity;
    }
}