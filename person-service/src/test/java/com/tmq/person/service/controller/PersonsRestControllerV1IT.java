package com.tmq.person.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmq.person.service.dto.*;
import com.tmq.person.service.model.IndividualsEntity;
import com.tmq.person.service.repository.IndividualsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
class PersonsRestControllerV1IT {

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        PostgresContainerBase.configureProperties(registry);
    }

    @Autowired
    private WebApplicationContext wac;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private IndividualsRepository individualsRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        individualsRepository.deleteAll();
    }

    private IndividualWriteDto buildRequest(String email) {
        AddressWriteDto address = new AddressWriteDto();
        address.setAddress("Test Street 1");
        address.setZipCode("10115");
        address.setCity("Berlin");
        address.setCountryCode("Germany");

        IndividualWriteDto dto = new IndividualWriteDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail(email);
        dto.setPassportNumber("AB123456");
        dto.setPhoneNumber("+49123456789");
        dto.setAddress(address);
        return dto;
    }

    private String registerAndGetId(String email) throws Exception {
        String body = mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest(email))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(body, IndividualWriteResponseDto.class).getId();
    }

    @Test
    void registration_whenValidRequest_returns201AndId() throws Exception {
        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest("john@example.com"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }

    @Test
    void registration_whenUnknownCountry_returns400() throws Exception {
        IndividualWriteDto dto = buildRequest("john@example.com");
        dto.getAddress().setCountryCode("UnknownCountry");

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(containsString("UnknownCountry")));
    }

    @Test
    void findById_whenExists_returns200WithDto() throws Exception {
        String id = registerAndGetId("find@example.com");

        mockMvc.perform(get("/api/v1/persons/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("find@example.com"))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void findById_whenNotFound_returns400() throws Exception {
        mockMvc.perform(get("/api/v1/persons/{id}", UUID.randomUUID()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(containsString("not found")));
    }

    @Test
    void findAllByEmail_whenEmailMatches_returns200WithItems() throws Exception {
        String email = "search@example.com";
        registerAndGetId(email);

        mockMvc.perform(get("/api/v1/persons").param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].email").value(email));
    }

    @Test
    void findAllByEmail_whenNoMatch_returns404() throws Exception {
        mockMvc.perform(get("/api/v1/persons").param("email", "nobody@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_whenExists_returns201WithSameId() throws Exception {
        String id = registerAndGetId("old@example.com");

        IndividualWriteDto updateDto = buildRequest("new@example.com");
        updateDto.setPassportNumber("ZZ999999");

        mockMvc.perform(put("/api/v1/persons/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void update_whenNotFound_returns400() throws Exception {
        mockMvc.perform(put("/api/v1/persons/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest("x@example.com"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_whenExists_returns200AndStatusIsBlocked() throws Exception {
        String id = registerAndGetId("soft-delete@example.com");

        mockMvc.perform(delete("/api/v1/persons/{id}", id))
                .andExpect(status().isOk());

        IndividualsEntity entity = individualsRepository
                .findById(UUID.fromString(id)).orElseThrow();
        assertThat(entity.getStatus()).isEqualTo("BLOCKED");
    }

    @Test
    void compensateRegistration_whenExists_returns200AndEntityIsGone() throws Exception {
        String id = registerAndGetId("hard-delete@example.com");

        mockMvc.perform(delete("/api/v1/persons/compensate-registration/{id}", id))
                .andExpect(status().isOk());

        assertThat(individualsRepository.findById(UUID.fromString(id))).isEmpty();
    }
}