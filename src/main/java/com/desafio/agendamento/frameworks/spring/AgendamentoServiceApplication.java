package com.desafio.agendamento.frameworks.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// Classe principal fica separada aqui pra deixar claro que so faz o boot do Spring,
// sem regra de negocio. Como ela nao esta na raiz do pacote base, tive que
// apontar manualmente onde ficam as entidades JPA e os repositorios.
@SpringBootApplication(scanBasePackages = "com.desafio.agendamento")
@EnableJpaRepositories(basePackages = "com.desafio.agendamento.adapters.out.persistence")
@EntityScan(basePackages = "com.desafio.agendamento.adapters.out.persistence")
@ConfigurationPropertiesScan(basePackages = "com.desafio.agendamento.frameworks.config")
public class AgendamentoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgendamentoServiceApplication.class, args);
    }
}


