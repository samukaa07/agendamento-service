package com.desafio.agendamento.usecases.impl;

import com.desafio.agendamento.entities.Agendamento;
import com.desafio.agendamento.entities.StatusAgendamento;
import com.desafio.agendamento.frameworks.exceptions.AgendamentoNaoEncontradoException;
import com.desafio.agendamento.usecases.ports.in.BuscarAgendamentoUseCase;
import com.desafio.agendamento.usecases.ports.out.AgendamentoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscarAgendamentoServiceTest {

    @Mock
    private AgendamentoRepositoryPort repository;

    private BuscarAgendamentoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new BuscarAgendamentoService(repository);
    }

    @Test
    void deveBuscarAgendamentoPorId() {
        Agendamento agendamento = Agendamento.novo("Roberto Alves", "12312312312",
                LocalDateTime.now().plusDays(3), null);

        when(repository.buscarPorId(agendamento.getId())).thenReturn(Optional.of(agendamento));

        Agendamento encontrado = useCase.buscarPorId(agendamento.getId());

        assertThat(encontrado.getId()).isEqualTo(agendamento.getId());
    }

    @Test
    void deveLancarExcecaoAoBuscarIdInexistente() {
        UUID idInexistente = UUID.randomUUID();
        when(repository.buscarPorId(idInexistente)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.buscarPorId(idInexistente))
                .isInstanceOf(AgendamentoNaoEncontradoException.class);
    }

    @Test
    void deveListarAgendamentosFiltrandoPorStatus() {
        Agendamento pendente = Agendamento.novo("Paciente Um", "11111111111", LocalDateTime.now().plusDays(1), null);

        when(repository.listar(StatusAgendamento.PENDENTE)).thenReturn(List.of(pendente));

        List<Agendamento> resultado = useCase.listar(StatusAgendamento.PENDENTE);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getStatus()).isEqualTo(StatusAgendamento.PENDENTE);
    }
}

