package com.prog4_tpi_grupo1.backend.shared.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

// url: http://localhost:8080/swagger-ui/index.html#/

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Prode Mundial API",
                version = "1.0",
                description = "API REST para sistema de pronósticos del Mundial"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {
}