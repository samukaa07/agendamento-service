package com.desafio.agendamento.adapters.out.persistence;

import com.desafio.agendamento.entities.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AgendamentoJpaRepository extends JpaRepository<AgendamentoJpaEntity, UUID> {

    List<AgendamentoJpaEntity> findByStatus(StatusAgendamento status);
}

