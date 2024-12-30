package com.example.pharma.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
	@Bean
	public OpenAPI usersMicroserviceOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Pharma Application").description("Pharma Utilities").version("1.0"));
	}
}