package com.desafio.agendamento.adapters.in.controller;

import com.desafio.agendamento.adapters.in.controller.dto.AgendamentoResponse;
import com.desafio.agendamento.adapters.in.controller.dto.ApiResponse;
import com.desafio.agendamento.adapters.in.controller.dto.AtualizarStatusRequest;
import com.desafio.agendamento.adapters.in.controller.dto.CriarAgendamentoRequest;
import com.desafio.agendamento.entities.Agendamento;
import com.desafio.agendamento.entities.StatusAgendamento;
import com.desafio.agendamento.usecases.ports.in.AtualizarStatusAgendamentoUseCase;
import com.desafio.agendamento.usecases.ports.in.BuscarAgendamentoUseCase;
import com.desafio.agendamento.usecases.ports.in.CriarAgendamentoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/agendamentos")
@Tag(name = "Agendamentos", description = "Gestao de agendamentos domiciliares")
public class AgendamentoController {

    private final CriarAgendamentoUseCase criarAgendamentoUseCase;
    private final BuscarAgendamentoUseCase buscarAgendamentoUseCase;
    private final AtualizarStatusAgendamentoUseCase atualizarStatusAgendamentoUseCase;

    public AgendamentoController(CriarAgendamentoUseCase criarAgendamentoUseCase,
                                  BuscarAgendamentoUseCase buscarAgendamentoUseCase,
                                  AtualizarStatusAgendamentoUseCase atualizarStatusAgendamentoUseCase) {
        this.criarAgendamentoUseCase = criarAgendamentoUseCase;
        this.buscarAgendamentoUseCase = buscarAgendamentoUseCase;
        this.atualizarStatusAgendamentoUseCase = atualizarStatusAgendamentoUseCase;
    }

    @Operation(summary = "Cria um novo agendamento")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AgendamentoResponse> criar(@Valid @RequestBody CriarAgendamentoRequest request) {
        CriarAgendamentoUseCase.Comando comando = new CriarAgendamentoUseCase.Comando(
                request.pacienteNome(),
                request.pacienteCpf(),
                request.dataAgendamento(),
                request.observacao()
        );

        CriarAgendamentoUseCase.Resultado resultado = criarAgendamentoUseCase.criar(comando);
        Agendamento criado = buscarAgendamentoUseCase.buscarPorId(resultado.id());

        return ApiResponse.of(AgendamentoResponse.from(criado), "Agendamento criado com sucesso");
    }

    @Operation(summary = "Lista agendamentos, com filtro opcional por status")
    @GetMapping
    public ApiResponse<List<AgendamentoResponse>> listar(
            @RequestParam(value = "status", required = false) StatusAgendamento status) {

        List<AgendamentoResponse> agendamentos = buscarAgendamentoUseCase.listar(status).stream()
                .map(AgendamentoResponse::from)
                .toList();

        return ApiResponse.ok(agendamentos);
    }

    @Operation(summary = "Busca um agendamento pelo id")
    @GetMapping("/{id}")
    public ApiResponse<AgendamentoResponse> buscarPorId(@PathVariable UUID id) {
        Agendamento agendamento = buscarAgendamentoUseCase.buscarPorId(id);
        return ApiResponse.ok(AgendamentoResponse.from(agendamento));
    }

    @Operation(summary = "Atualiza o status de um agendamento")
    @PatchMapping("/{id}/status")
    public ApiResponse<AgendamentoResponse> atualizarStatus(@PathVariable UUID id,
                                                              @Valid @RequestBody AtualizarStatusRequest request) {
        Agendamento atualizado = atualizarStatusAgendamentoUseCase.atualizarStatus(
                id, request.status(), request.observacao());

        return ApiResponse.of(AgendamentoResponse.from(atualizado), "Status atualizado com sucesso");
    }
}

