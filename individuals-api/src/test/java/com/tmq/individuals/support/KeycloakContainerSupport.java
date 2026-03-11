package com.tmq.individuals.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Testcontainers
public abstract class KeycloakContainerSupport extends BaseIntegrationSupport {

    protected static final String REALM = "payment-system";
    protected static final String CLIENT_UUID = "e70d7d14-3756-488f-9abc-245aa788995c";

    @Container
    public static final KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:26.2")
            .withRealmImportFile("realm-config.json");

    @DynamicPropertySource
    public static void configureKeycloakProperties(DynamicPropertyRegistry registry) {
        String baseUrl = keycloak.getAuthServerUrl();
        String clientSecret = regenerateClientSecret(baseUrl);

        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> baseUrl + "/realms/" + REALM);
        registry.add("spring.security.oauth2.client.provider.keycloak.issuer-uri",
                () -> baseUrl + "/realms/" + REALM);
        registry.add("spring.security.oauth2.client.registration.keycloak.client-secret",
                () -> clientSecret);
        registry.add("keycloak.url", () -> "http://" + keycloak.getHost());
        registry.add("keycloak.port", () -> String.valueOf(keycloak.getMappedPort(8080)));
    }

    private static String regenerateClientSecret(String keycloakBaseUrl) {
        try {
            var http = HttpClient.newHttpClient();
            var mapper = new ObjectMapper();

            String formBody = "grant_type=password&client_id=admin-cli&username=%s&password=%s"
                    .formatted(keycloak.getAdminUsername(), keycloak.getAdminPassword());

            var tokenResp = http.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create(keycloakBaseUrl + "/realms/master/protocol/openid-connect/token"))
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .POST(HttpRequest.BodyPublishers.ofString(formBody))
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );

            String adminToken = mapper.readTree(tokenResp.body()).get("access_token").asText();

            var secretResp = http.send(
                    HttpRequest.newBuilder()
                            .uri(URI.create(keycloakBaseUrl + "/admin/realms/" + REALM
                                    + "/clients/" + CLIENT_UUID + "/client-secret"))
                            .header("Authorization", "Bearer " + adminToken)
                            .POST(HttpRequest.BodyPublishers.noBody())
                            .build(),
                    HttpResponse.BodyHandlers.ofString()
            );

            return mapper.readTree(secretResp.body()).get("value").asText();
        } catch (Exception e) {
            throw new RuntimeException("Failed to regenerate Keycloak client secret", e);
        }
    }

    static {
        keycloak.start();
    }
}