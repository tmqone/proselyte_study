package com.tmq.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JacksonMapperUtil {
    @Getter
    private final static ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
            .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, true)
            .setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());

}
