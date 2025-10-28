package com.apikeygen.apikeygenerator;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApikeyGeneratorApplication {

	public static void main(String[] args) {
        Dotenv.load().entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(ApikeyGeneratorApplication.class, args);
	}

}
