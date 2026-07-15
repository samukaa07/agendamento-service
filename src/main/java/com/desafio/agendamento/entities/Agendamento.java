package com.desafio.agendamento.entities;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

// Entidade de dominio do agendamento. Sem anotacao de framework nenhuma
// (nada de JPA/Spring aqui) - as regras de negocio ficam todas dentro dela.
public class Agendamento {

    private static final Pattern CPF_SOMENTE_DIGITOS = Pattern.compile("^\\d{11}$");
    private static final int TAMANHO_MINIMO_NOME = 3;

    private final UUID id;
    private String pacienteNome;
    private String pacienteCpf;
    private LocalDateTime dataAgendamento;
    private StatusAgendamento status;
    private String observacao;
    private final LocalDateTime criadoEm;

    private Agendamento(UUID id, String pacienteNome, String pacienteCpf, LocalDateTime dataAgendamento,
                         StatusAgendamento status, String observacao, LocalDateTime criadoEm) {
        this.id = id;
        this.pacienteNome = pacienteNome;
        this.pacienteCpf = pacienteCpf;
        this.dataAgendamento = dataAgendamento;
        this.status = status;
        this.observacao = observacao;
        this.criadoEm = criadoEm;
    }

    // cria um agendamento novo ja com status PENDENTE e valida as regras de negocio
    public static Agendamento novo(String pacienteNome, String pacienteCpf, LocalDateTime dataAgendamento,
                                    String observacao) {
        validarNome(pacienteNome);
        validarCpf(pacienteCpf);
        validarDataFutura(dataAgendamento);

        return new Agendamento(
                UUID.randomUUID(),
                pacienteNome.trim(),
                pacienteCpf,
                dataAgendamento,
                StatusAgendamento.PENDENTE,
                observacao,
                LocalDateTime.now()
        );
    }

    // usado pelo adapter de persistencia pra reconstituir um agendamento que ja existe no banco
    public static Agendamento restaurar(UUID id, String pacienteNome, String pacienteCpf,
                                         LocalDateTime dataAgendamento, StatusAgendamento status,
                                         String observacao, LocalDateTime criadoEm) {
        return new Agendamento(id, pacienteNome, pacienteCpf, dataAgendamento, status, observacao, criadoEm);
    }

    // cancelado nao muda mais de status, e cancelar sem observacao nao pode
    public void alterarStatus(StatusAgendamento novoStatus, String novaObservacao) {
        Objects.requireNonNull(novoStatus, "status e obrigatorio");

        if (this.status == StatusAgendamento.CANCELADO) {
            throw new IllegalStateException("Nao e possivel alterar o status de um agendamento ja cancelado");
        }

        if (novoStatus == StatusAgendamento.CANCELADO && (novaObservacao == null || novaObservacao.isBlank())) {
            throw new IllegalArgumentException("Ao cancelar um agendamento a observacao e obrigatoria");
        }

        this.status = novoStatus;
        if (novaObservacao != null && !novaObservacao.isBlank()) {
            this.observacao = novaObservacao;
        }
    }

    private static void validarNome(String nome) {
        if (nome == null || nome.trim().length() < TAMANHO_MINIMO_NOME) {
            throw new IllegalArgumentException("Nome do paciente deve ter no minimo " + TAMANHO_MINIMO_NOME + " caracteres");
        }
    }

    private static void validarCpf(String cpf) {
        if (cpf == null || !CPF_SOMENTE_DIGITOS.matcher(cpf).matches()) {
            throw new IllegalArgumentException("CPF invalido: informe apenas os 11 digitos");
        }
    }

    private static void validarDataFutura(LocalDateTime data) {
        if (data == null) {
            throw new IllegalArgumentException("Data do agendamento e obrigatoria");
        }
        if (data.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Data do agendamento nao pode ser no passado");
        }
    }

    public UUID getId() {
        return id;
    }

    public String getPacienteNome() {
        return pacienteNome;
    }

    public String getPacienteCpf() {
        return pacienteCpf;
    }

    public LocalDateTime getDataAgendamento() {
        return dataAgendamento;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public String getObservacao() {
        return observacao;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agendamento that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

