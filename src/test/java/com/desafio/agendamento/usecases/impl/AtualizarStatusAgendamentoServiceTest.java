package com.desafio.agendamento.usecases.impl;

import com.desafio.agendamento.entities.Agendamento;
import com.desafio.agendamento.entities.StatusAgendamento;
import com.desafio.agendamento.frameworks.exceptions.AgendamentoNaoEncontradoException;
import com.desafio.agendamento.frameworks.exceptions.RegraDeNegocioException;
import com.desafio.agendamento.usecases.ports.in.AtualizarStatusAgendamentoUseCase;
import com.desafio.agendamento.usecases.ports.out.AgendamentoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarStatusAgendamentoServiceTest {

    @Mock
    private AgendamentoRepositoryPort repository;

    private AtualizarStatusAgendamentoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new AtualizarStatusAgendamentoService(repository);
    }

    @Test
    void deveAtualizarStatusParaConfirmado() {
        Agendamento agendamento = Agendamento.novo("Ana Paula", "11122233344",
                LocalDateTime.now().plusDays(1), null);

        when(repository.buscarPorId(agendamento.getId())).thenReturn(Optional.of(agendamento));
        when(repository.salvar(any(Agendamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Agendamento atualizado = useCase.atualizarStatus(agendamento.getId(), StatusAgendamento.CONFIRMADO, null);

        assertThat(atualizado.getStatus()).isEqualTo(StatusAgendamento.CONFIRMADO);
    }

    @Test
    void naoDeveCancelarAgendamentoSemObservacao() {
        Agendamento agendamento = Agendamento.novo("Carlos Lima", "55566677788",
                LocalDateTime.now().plusDays(1), null);

        when(repository.buscarPorId(agendamento.getId())).thenReturn(Optional.of(agendamento));

        assertThatThrownBy(() -> useCase.atualizarStatus(agendamento.getId(), StatusAgendamento.CANCELADO, null))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("observacao");
    }

    @Test
    void naoDeveAlterarStatusDeAgendamentoJaCancelado() {
        Agendamento agendamento = Agendamento.novo("Fernanda Reis", "99988877766",
                LocalDateTime.now().plusDays(1), null);
        agendamento.alterarStatus(StatusAgendamento.CANCELADO, "Paciente desistiu");

        when(repository.buscarPorId(agendamento.getId())).thenReturn(Optional.of(agendamento));

        assertThatThrownBy(() -> useCase.atualizarStatus(agendamento.getId(), StatusAgendamento.CONFIRMADO, null))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("cancelado");
    }

    @Test
    void deveLancarExcecaoQuandoAgendamentoNaoExiste() {
        UUID idInexistente = UUID.randomUUID();
        when(repository.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.atualizarStatus(idInexistente, StatusAgendamento.CONFIRMADO, null))
                .isInstanceOf(AgendamentoNaoEncontradoException.class);
    }
}

