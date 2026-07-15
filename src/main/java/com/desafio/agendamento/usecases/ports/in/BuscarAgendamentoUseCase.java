package com.desafio.agendamento.usecases.ports.in;

import com.desafio.agendamento.entities.Agendamento;
import com.desafio.agendamento.entities.StatusAgendamento;

import java.util.List;
import java.util.UUID;

// busca por id e listagem, com filtro opcional por status
public interface BuscarAgendamentoUseCase {

    Agendamento buscarPorId(UUID id);

    List<Agendamento> listar(StatusAgendamento statusFiltro);
}

