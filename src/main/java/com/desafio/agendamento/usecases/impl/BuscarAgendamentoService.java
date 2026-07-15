package com.desafio.agendamento.usecases.impl;

import com.desafio.agendamento.entities.Agendamento;
import com.desafio.agendamento.entities.StatusAgendamento;
import com.desafio.agendamento.frameworks.exceptions.AgendamentoNaoEncontradoException;
import com.desafio.agendamento.usecases.ports.in.BuscarAgendamentoUseCase;
import com.desafio.agendamento.usecases.ports.out.AgendamentoRepositoryPort;

import java.util.List;
import java.util.UUID;

public class BuscarAgendamentoService implements BuscarAgendamentoUseCase {

    private final AgendamentoRepositoryPort repository;

    public BuscarAgendamentoService(AgendamentoRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Agendamento buscarPorId(UUID id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new AgendamentoNaoEncontradoException(id));
    }

    @Override
    public List<Agendamento> listar(StatusAgendamento statusFiltro) {
        return repository.listar(statusFiltro);
    }
}

