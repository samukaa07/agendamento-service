package com.desafio.agendamento.frameworks.exceptions;

// disparada quando alguma regra de negocio e violada (data no passado, cancelado
// que nao pode mudar status, etc)
public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}

