package com.tmq.individuals_api;

import com.tmq.individuals_api.client.KeycloakClient;
import com.tmq.individuals_api.dto.TokenResponse;
import com.tmq.individuals_api.dto.UserRegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@SpringBootApplication
public class IndividualsApiApplication {
	private static final Logger log = LoggerFactory.getLogger(IndividualsApiApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(IndividualsApiApplication.class, args);
	}

	public static void test () {
		while (true) {
			UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();
			userRegistrationRequest.setEmail("%s@gmail.com".formatted(UUID.randomUUID().toString()));
			userRegistrationRequest.setPassword("123456");
			userRegistrationRequest.setConfirmPassword("123456");
			WebClient.create("http://localhost:8081")
					.post()
					.uri("/api/v1/auth/registration")
					.bodyValue(userRegistrationRequest)
					.retrieve()
					.bodyToMono(TokenResponse.class)
					.flatMap(tokenResponse -> {
						log.info("token response: {}", tokenResponse);
						return Mono.just(tokenResponse);
					})
					.subscribe();
			log.info("Был отправлен запрос {}", userRegistrationRequest);
        }
	}
}
