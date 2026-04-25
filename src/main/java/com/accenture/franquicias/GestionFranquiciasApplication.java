package com.accenture.franquicias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.mongodb.autoconfigure.MongoReactiveAutoConfiguration;

@SpringBootApplication // (exclude = {MongoReactiveAutoConfiguration.class})
public class GestionFranquiciasApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionFranquiciasApplication.class, args);
	}

}
