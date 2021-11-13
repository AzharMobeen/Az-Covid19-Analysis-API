package com.az.covid19.analysis;

import com.az.covid19.analysis.constant.AppConstants;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = AppConstants.API_TITLE, version = AppConstants.API_VERSION,
		description = AppConstants.API_DESCRIPTION))
	@SecurityScheme(name = AppConstants.SECURITY_SCHEME_NAME, scheme = AppConstants.SECURITY_SCHEME,
		type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class AzCovid19AnalysisApplication {

	public static void main(String[] args) {
		SpringApplication.run(AzCovid19AnalysisApplication.class, args);
	}

}
