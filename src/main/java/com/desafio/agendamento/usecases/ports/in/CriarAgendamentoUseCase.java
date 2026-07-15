package com.desafio.agendamento.usecases.ports.in;

import java.time.LocalDateTime;

public interface CriarAgendamentoUseCase {

    Resultado criar(Comando comando);

    // dados de entrada ja desacoplados do DTO web
    record Comando(String pacienteNome, String pacienteCpf, LocalDateTime dataAgendamento, String observacao) {
    }

    // so o id gerado mesmo, nao precisa devolver a entidade inteira aqui
    record Resultado(java.util.UUID id) {
    }
}

