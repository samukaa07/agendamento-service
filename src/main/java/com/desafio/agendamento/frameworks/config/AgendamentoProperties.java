package com.desafio.agendamento.frameworks.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// propriedade de exemplo configuravel via application.properties (prefixo "agendamento")
@ConfigurationProperties(prefix = "agendamento")
public class AgendamentoProperties {

    private String nomeServico = "Agendamento Service";

    public String getNomeServico() {
        return nomeServico;
    }

    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }
}

