package com.desafio.agendamento.usecases.impl;

import com.desafio.agendamento.entities.Agendamento;
import com.desafio.agendamento.entities.StatusAgendamento;
import com.desafio.agendamento.frameworks.exceptions.AgendamentoNaoEncontradoException;
import com.desafio.agendamento.frameworks.exceptions.RegraDeNegocioException;
import com.desafio.agendamento.usecases.ports.in.AtualizarStatusAgendamentoUseCase;
import com.desafio.agendamento.usecases.ports.out.AgendamentoRepositoryPort;

import java.util.UUID;

public class AtualizarStatusAgendamentoService implements AtualizarStatusAgendamentoUseCase {

    private final AgendamentoRepositoryPort repository;

    public AtualizarStatusAgendamentoService(AgendamentoRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Agendamento atualizarStatus(UUID id, StatusAgendamento novoStatus, String observacao) {
        Agendamento agendamento = repository.buscarPorId(id)
                .orElseThrow(() -> new AgendamentoNaoEncontradoException(id));

        try {
            agendamento.alterarStatus(novoStatus, observacao);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new RegraDeNegocioException(e.getMessage());
        }

        return repository.salvar(agendamento);
    }
}

