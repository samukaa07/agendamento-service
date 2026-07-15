package com.desafio.agendamento.usecases.ports.out;

import com.desafio.agendamento.entities.Agendamento;
import com.desafio.agendamento.entities.StatusAgendamento;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// contrato de persistencia - quem implementa de verdade e o adapter la fora (JPA, etc)
public interface AgendamentoRepositoryPort {

    Agendamento salvar(Agendamento agendamento);

    Optional<Agendamento> buscarPorId(UUID id);

    List<Agendamento> listar(StatusAgendamento statusFiltro);
}

