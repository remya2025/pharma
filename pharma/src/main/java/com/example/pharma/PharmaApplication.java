package com.example.pharma;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PharmaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PharmaApplication.class, args);
	}

}
