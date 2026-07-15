package com.desafio.agendamento.adapters.in.controller.dto;

import com.desafio.agendamento.entities.StatusAgendamento;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusRequest(

        @NotNull(message = "status e obrigatorio")
        StatusAgendamento status,

        String observacao
) {
}

