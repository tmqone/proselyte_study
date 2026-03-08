package com.tmq.individuals.support;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

public abstract class PersonServiceWireMockSupport extends BaseIntegrationSupport {

    public static final WireMockServer personServiceMock =
            new WireMockServer(WireMockConfiguration.options().dynamicPort());

    static {
        personServiceMock.start();
    }

    public static int getPort() {
        return personServiceMock.port();
    }

    public static void stop() {
        personServiceMock.stop();
    }

    public static void setupDefaultStubs() {
        personServiceMock.resetAll();

        personServiceMock.stubFor(post(urlEqualTo("/api/v1/persons"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": \"" + UUID.randomUUID() + "\"}")));

        personServiceMock.stubFor(delete(urlMatching("/api/v1/persons/compensate-registration/.*"))
                .willReturn(aResponse().withStatus(200)));
    }
}