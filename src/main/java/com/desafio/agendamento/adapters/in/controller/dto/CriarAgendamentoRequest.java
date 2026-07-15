package com.desafio.agendamento.adapters.in.controller.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CriarAgendamentoRequest(

        @NotBlank(message = "pacienteNome e obrigatorio")
        @Size(min = 3, message = "pacienteNome deve ter no minimo 3 caracteres")
        String pacienteNome,

        @NotBlank(message = "pacienteCpf e obrigatorio")
        @Pattern(regexp = "\\d{11}", message = "pacienteCpf deve conter exatamente 11 digitos numericos")
        String pacienteCpf,

        @NotNull(message = "dataAgendamento e obrigatorio")
        @Future(message = "dataAgendamento nao pode ser no passado")
        LocalDateTime dataAgendamento,

        String observacao
) {
}

