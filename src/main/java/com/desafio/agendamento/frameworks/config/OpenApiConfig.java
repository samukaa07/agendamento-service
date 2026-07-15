package com.desafio.agendamento.frameworks.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// config basica do swagger, disponivel em /swagger-ui.html
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI agendamentoOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Agendamento Service API")
                        .description("Microservico de gestao de agendamentos domiciliares")
                        .version("v1")
                        .contact(new Contact().name("Equipe de Desenvolvimento")));
    }
}

