package com.desafio.agendamento.usecases.impl;

import com.desafio.agendamento.entities.Agendamento;
import com.desafio.agendamento.frameworks.exceptions.RegraDeNegocioException;
import com.desafio.agendamento.usecases.ports.in.CriarAgendamentoUseCase;
import com.desafio.agendamento.usecases.ports.out.AgendamentoRepositoryPort;

// a validacao de negocio fica na entidade Agendamento, aqui e so
// converter os erros de validacao em RegraDeNegocioException
public class CriarAgendamentoService implements CriarAgendamentoUseCase {

    private final AgendamentoRepositoryPort repository;

    public CriarAgendamentoService(AgendamentoRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Resultado criar(Comando comando) {
        try {
            Agendamento agendamento = Agendamento.novo(
                    comando.pacienteNome(),
                    comando.pacienteCpf(),
                    comando.dataAgendamento(),
                    comando.observacao()
            );

            Agendamento salvo = repository.salvar(agendamento);
            return new Resultado(salvo.getId());
        } catch (IllegalArgumentException e) {
            throw new RegraDeNegocioException(e.getMessage());
        }
    }
}

