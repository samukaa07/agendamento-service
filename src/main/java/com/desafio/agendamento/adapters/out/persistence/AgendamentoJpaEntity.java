package com.desafio.agendamento.adapters.out.persistence;

import com.desafio.agendamento.entities.StatusAgendamento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

// entidade JPA - fica separada da entidade de dominio pra nao vazar anotacao de framework
@Entity
@Table(name = "agendamentos")
public class AgendamentoJpaEntity {

    @Id
    private UUID id;

    @Column(name = "paciente_nome", nullable = false, length = 150)
    private String pacienteNome;

    @Column(name = "paciente_cpf", nullable = false, length = 11)
    private String pacienteCpf;

    @Column(name = "data_agendamento", nullable = false)
    private LocalDateTime dataAgendamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusAgendamento status;

    @Column(name = "observacao", length = 500)
    private String observacao;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    protected AgendamentoJpaEntity() {
        // exigido pelo JPA
    }

    public AgendamentoJpaEntity(UUID id, String pacienteNome, String pacienteCpf, LocalDateTime dataAgendamento,
                                 StatusAgendamento status, String observacao, LocalDateTime criadoEm) {
        this.id = id;
        this.pacienteNome = pacienteNome;
        this.pacienteCpf = pacienteCpf;
        this.dataAgendamento = dataAgendamento;
        this.status = status;
        this.observacao = observacao;
        this.criadoEm = criadoEm;
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
}

