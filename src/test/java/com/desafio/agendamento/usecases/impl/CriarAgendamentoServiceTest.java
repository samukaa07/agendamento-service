package com.desafio.agendamento.usecases.impl;

import com.desafio.agendamento.entities.Agendamento;
import com.desafio.agendamento.frameworks.exceptions.RegraDeNegocioException;
import com.desafio.agendamento.usecases.ports.in.CriarAgendamentoUseCase;
import com.desafio.agendamento.usecases.ports.out.AgendamentoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarAgendamentoServiceTest {

    @Mock
    private AgendamentoRepositoryPort repository;

    private CriarAgendamentoUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CriarAgendamentoService(repository);
    }

    @Test
    void deveCriarAgendamentoComSucessoQuandoDadosValidos() {
        LocalDateTime dataFutura = LocalDateTime.now().plusDays(2);
        var comando = new CriarAgendamentoUseCase.Comando("Maria da Silva", "12345678901", dataFutura, null);

        when(repository.salvar(any(Agendamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CriarAgendamentoUseCase.Resultado resultado = useCase.criar(comando);

        assertThat(resultado.id()).isNotNull();

        ArgumentCaptor<Agendamento> captor = ArgumentCaptor.forClass(Agendamento.class);
        verify(repository).salvar(captor.capture());

        Agendamento salvo = captor.getValue();
        assertThat(salvo.getPacienteNome()).isEqualTo("Maria da Silva");
        assertThat(salvo.getPacienteCpf()).isEqualTo("12345678901");
        assertThat(salvo.getStatus().name()).isEqualTo("PENDENTE");
    }

    @Test
    void naoDeveCriarAgendamentoComDataNoPassado() {
        LocalDateTime dataPassada = LocalDateTime.now().minusDays(1);
        var comando = new CriarAgendamentoUseCase.Comando("Joao Souza", "98765432100", dataPassada, null);

        assertThatThrownBy(() -> useCase.criar(comando))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("passado");
    }

    @Test
    void naoDeveCriarAgendamentoComCpfInvalido() {
        LocalDateTime dataFutura = LocalDateTime.now().plusDays(1);
        var comando = new CriarAgendamentoUseCase.Comando("Joao Souza", "123", dataFutura, null);

        assertThatThrownBy(() -> useCase.criar(comando))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("CPF");
    }

    @Test
    void naoDeveCriarAgendamentoComNomeMuitoCurto() {
        LocalDateTime dataFutura = LocalDateTime.now().plusDays(1);
        var comando = new CriarAgendamentoUseCase.Comando("Jo", "12345678901", dataFutura, null);

        assertThatThrownBy(() -> useCase.criar(comando))
                .isInstanceOf(RegraDeNegocioException.class);
    }
}

