package com.desafio.agendamento.frameworks.spring;

import com.desafio.agendamento.adapters.in.controller.dto.CriarAgendamentoRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// sobe o contexto inteiro (com H2 real) e bate na API via MockMvc, sem mock nenhum
@SpringBootTest
@AutoConfigureMockMvc
class AgendamentoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarEDepoisListarAgendamento() throws Exception {
        CriarAgendamentoRequest request = new CriarAgendamentoRequest(
                "Paciente Integracao", "12345678901", LocalDateTime.now().plusDays(5), "Primeira consulta");

        mockMvc.perform(post("/api/v1/agendamentos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.pacienteNome").value("Paciente Integracao"))
                .andExpect(jsonPath("$.data.status").value("PENDENTE"));

        mockMvc.perform(get("/api/v1/agendamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void deveRetornarBadRequestQuandoCpfInvalido() throws Exception {
        CriarAgendamentoRequest request = new CriarAgendamentoRequest(
                "Paciente Invalido", "abc", LocalDateTime.now().plusDays(5), null);

        mockMvc.perform(post("/api/v1/agendamentos")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}

