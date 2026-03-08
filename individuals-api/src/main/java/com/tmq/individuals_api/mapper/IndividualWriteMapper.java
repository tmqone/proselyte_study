package com.tmq.individuals_api.mapper;

import com.tmq.person.dto.IndividualWriteDto;
import com.tmq.individuals.dto.UserRegistrationRequest;
import org.springframework.stereotype.Component;

@Component
public class IndividualWriteMapper {

    public IndividualWriteDto toIndividualWriteDto(UserRegistrationRequest request) {
        IndividualWriteDto dto = new IndividualWriteDto();
        dto.setFirstName(request.getFirstName());
        dto.setLastName(request.getLastName());
        dto.setEmail(request.getEmail());
        dto.setPassportNumber(request.getPassportNumber());
        dto.setPhoneNumber(request.getPhoneNumber());
        dto.setAddress(toAddressWriteDto(request.getAddress()));
        return dto;
    }

    private com.tmq.person.dto.AddressWriteDto toAddressWriteDto(com.tmq.individuals.dto.AddressWriteDto src) {
        if (src == null) return null;
        com.tmq.person.dto.AddressWriteDto dest = new com.tmq.person.dto.AddressWriteDto();
        dest.setAddress(src.getAddress());
        dest.setZipCode(src.getZipCode());
        dest.setCity(src.getCity());
        dest.setCountryCode(src.getCountryCode());
        return dest;
    }
}