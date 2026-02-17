package com.tmq.individuals_api.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KeycloakClient {

    private final WebClient keycloakWebClient;
}
