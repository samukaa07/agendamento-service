package com.desafio.agendamento.frameworks.config;

import com.desafio.agendamento.usecases.impl.AtualizarStatusAgendamentoService;
import com.desafio.agendamento.usecases.impl.BuscarAgendamentoService;
import com.desafio.agendamento.usecases.impl.CriarAgendamentoService;
import com.desafio.agendamento.usecases.ports.in.AtualizarStatusAgendamentoUseCase;
import com.desafio.agendamento.usecases.ports.in.BuscarAgendamentoUseCase;
import com.desafio.agendamento.usecases.ports.in.CriarAgendamentoUseCase;
import com.desafio.agendamento.usecases.ports.out.AgendamentoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// para lembrar das interfaces (ports) e as implementacoes dos use cases. Preferi nao
// colocar @Service direto nas classes de impl pra manter aquele pacote livre de
// anotacao do Spring - a ideia toda fica concentrada aqui
@Configuration
public class UseCaseConfig {

    @Bean
    public CriarAgendamentoUseCase criarAgendamentoUseCase(AgendamentoRepositoryPort repository) {
        return new CriarAgendamentoService(repository);
    }

    @Bean
    public BuscarAgendamentoUseCase buscarAgendamentoUseCase(AgendamentoRepositoryPort repository) {
        return new BuscarAgendamentoService(repository);
    }

    @Bean
    public AtualizarStatusAgendamentoUseCase atualizarStatusAgendamentoUseCase(AgendamentoRepositoryPort repository) {
        return new AtualizarStatusAgendamentoService(repository);
    }
}

