package com.tmq.individuals_api;

import com.tmq.individuals_api.client.KeycloakClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IndividualsApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(IndividualsApiApplication.class, args);
	}
}
