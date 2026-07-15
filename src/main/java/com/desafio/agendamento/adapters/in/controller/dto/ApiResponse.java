package com.desafio.agendamento.adapters.in.controller.dto;

import java.time.LocalDateTime;

// envelope padrao usado em todas as respostas: data + message + timestamp
public record ApiResponse<T>(T data, String message, LocalDateTime timestamp) {

    public static <T> ApiResponse<T> of(T data, String message) {
        return new ApiResponse<>(data, message, LocalDateTime.now());
    }

    public static <T> ApiResponse<T> ok(T data) {
        return of(data, "Sucesso");
    }
}

