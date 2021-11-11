package com.az.covid19.analysis;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Covid19 Analysis API", version = "1.0", description = "In this API we tried to give data for World wide confirm Covid19 cases for analysis"))
	@SecurityScheme(name = "az-covid19-analysis", scheme = "Bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class AzCovid19AnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(AzCovid19AnalysisApplication.class, args);
	}
}
