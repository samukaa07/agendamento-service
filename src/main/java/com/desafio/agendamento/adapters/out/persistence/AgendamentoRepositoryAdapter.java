package com.desafio.agendamento.adapters.out.persistence;

import com.desafio.agendamento.entities.Agendamento;
import com.desafio.agendamento.entities.StatusAgendamento;
import com.desafio.agendamento.usecases.ports.out.AgendamentoRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// implementacao do port usando JPA/H2 - converte entidade de dominio <-> entidade JPA
@Component
public class AgendamentoRepositoryAdapter implements AgendamentoRepositoryPort {

    private final AgendamentoJpaRepository jpaRepository;

    public AgendamentoRepositoryAdapter(AgendamentoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Agendamento salvar(Agendamento agendamento) {
        AgendamentoJpaEntity entidade = paraJpa(agendamento);
        AgendamentoJpaEntity salva = jpaRepository.save(entidade);
        return paraDominio(salva);
    }

    @Override
    public Optional<Agendamento> buscarPorId(UUID id) {
        return jpaRepository.findById(id).map(this::paraDominio);
    }

    @Override
    public List<Agendamento> listar(StatusAgendamento statusFiltro) {
        List<AgendamentoJpaEntity> entidades = statusFiltro == null
                ? jpaRepository.findAll()
                : jpaRepository.findByStatus(statusFiltro);

        return entidades.stream().map(this::paraDominio).toList();
    }

    private AgendamentoJpaEntity paraJpa(Agendamento agendamento) {
        return new AgendamentoJpaEntity(
                agendamento.getId(),
                agendamento.getPacienteNome(),
                agendamento.getPacienteCpf(),
                agendamento.getDataAgendamento(),
                agendamento.getStatus(),
                agendamento.getObservacao(),
                agendamento.getCriadoEm()
        );
    }

    private Agendamento paraDominio(AgendamentoJpaEntity entidade) {
        return Agendamento.restaurar(
                entidade.getId(),
                entidade.getPacienteNome(),
                entidade.getPacienteCpf(),
                entidade.getDataAgendamento(),
                entidade.getStatus(),
                entidade.getObservacao(),
                entidade.getCriadoEm()
        );
    }
}

