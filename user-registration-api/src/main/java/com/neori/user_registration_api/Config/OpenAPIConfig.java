package com.neori.user_registration_api.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API de Registro de Usuarios. Prueba BCI",
        version = "1.0",
        description = "Documentación de la API para registrar y administrar usuarios. Prueba técnica sebastián picardo"
    )
)

public class OpenAPIConfig {
}