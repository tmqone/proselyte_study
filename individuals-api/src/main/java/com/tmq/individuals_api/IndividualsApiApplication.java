package com.tmq.individuals_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import reactor.core.publisher.Hooks;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.tmq.common.api")
public class IndividualsApiApplication {

	public static void main(String[] args) {
		Hooks.enableAutomaticContextPropagation();
		SpringApplication.run(IndividualsApiApplication.class, args);
	}
}
