package com.tmq.individuals_api.mapper;

import com.tmq.common.dto.IndividualWriteDto;
import com.tmq.common.dto.UserRegistrationRequest;
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
        dto.setAddress(request.getAddress());
        return dto;
    }
}