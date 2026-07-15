package com.desafio.agendamento.frameworks.exceptions;

import java.util.UUID;

public class AgendamentoNaoEncontradoException extends RuntimeException {

    public AgendamentoNaoEncontradoException(UUID id) {
        super("Agendamento nao encontrado para o id: " + id);
    }
}

