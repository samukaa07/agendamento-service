# Agendamento Service

API para gestão de agendamentos de atendimento domiciliar. Feito como desafio
técnico, seguindo Clean Architecture / arquitetura hexagonal.

## Stack

- Java 21
- Spring Boot 3.3.4 (Web, Data JPA, Validation)
- H2 em memória (não precisa Docker, não precisa instalar banco)
- Maven
- SpringDoc/Swagger
- JUnit 5 + Mockito + AssertJ

## Rodando o projeto

Precisa ter Java 21 instalado. O Maven vem embutido no wrapper, não precisa
instalar nada além do JDK.

```bash
# Linux/Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

Sobe em `http://localhost:8080`.

Rodar os testes:

```bash
./mvnw test
```

### Swagger

Com a aplicação no ar:

```
http://localhost:8080/swagger-ui.html
```

### Console do H2

`http://localhost:8080/h2-console`
JDBC URL: `jdbc:h2:mem:agendamentodb`, user `sa`, sem senha.

## Endpoints

Toda resposta segue o mesmo envelope:

```json
{
  "data": { ... },
  "message": "Sucesso",
  "timestamp": "2026-07-15T10:00:00"
}
```

### Criar agendamento

`POST /api/v1/agendamentos`

```bash
curl -X POST http://localhost:8080/api/v1/agendamentos \
  -H "Content-Type: application/json" \
  -d '{
    "pacienteNome": "Samuel Pereira Lima",
    "pacienteCpf": "38078977808",
    "dataAgendamento": "2026-08-20T14:30:00",
    "observacao": "Primeira consulta domiciliar"
  }'
```

### Listar (com filtro opcional por status)

`GET /api/v1/agendamentos`
`GET /api/v1/agendamentos?status=PENDENTE`

```bash
curl http://localhost:8080/api/v1/agendamentos
curl http://localhost:8080/api/v1/agendamentos?status=CONFIRMADO
```

### Buscar por id

`GET /api/v1/agendamentos/{id}`

```bash
curl http://localhost:8080/api/v1/agendamentos/3fa85f64-5717-4562-b3fc-2c963f66afa6
```

### Atualizar status

`PATCH /api/v1/agendamentos/{id}/status`

```bash
curl -X PATCH http://localhost:8080/api/v1/agendamentos/3fa85f64-5717-4562-b3fc-2c963f66afa6/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "CANCELADO",
    "observacao": "Paciente remarcou"
  }'
```

## Regras de negócio

- Não dá pra criar agendamento com data no passado.
- Agendamento CANCELADO não muda mais de status.
- CPF só é validado de forma simples (11 dígitos numéricos).
- Cancelar sem informar observação não é permitido.
- Todo agendamento nasce PENDENTE, com `criadoEm` preenchido na hora.

## Sobre a arquitetura

Estrutura de pacotes (hexagonal):

- `entities` – a classe `Agendamento` é Java puro, sem nenhuma anotação de
  Spring/JPA. As regras de negócio (validação de nome, CPF, data, transição de
  status) ficam todas ali dentro. Uso dois factory methods (`novo` e
  `restaurar`) em vez de construtor público: `novo` valida tudo porque é uma
  criação de verdade, `restaurar` só remonta o objeto que já veio validado do
  banco.

- `usecases/ports/in` e `ports/out` – interfaces que separam o "o que a
  aplicação faz" (casos de uso) do "como ela persiste dados" (repository
  port). O controller só conhece as interfaces de entrada, nunca a
  implementação.

- `usecases/impl` – implementação dos casos de uso. Deixei essas classes sem
  `@Service`/`@Component` de propósito, pra não misturar anotação de Spring
  com lógica de aplicação. Quem registra os beans é a `UseCaseConfig`.

- `adapters/in/controller` – controller + DTOs (usei `record` pros DTOs). 
- O `ApiResponse<T>` é o envelope padrão pedido no desafio.

- `adapters/out/persistence` – entidade JPA, repositório Spring Data e o
  adapter que converte entre entidade JPA e entidade de domínio. É o único
  lugar que conhece os dois lados.

- `frameworks` – dividido em `spring` (classe main), `config` (beans,
  Swagger, um `@ConfigurationProperties` de exemplo) e `exceptions` (exceções
  próprias + `@RestControllerAdvice`).

Sobre erro: em vez de deixar `IllegalArgumentException`/`IllegalStateException`
do domínio estourarem até o controller, criei duas exceções de aplicação
(`AgendamentoNaoEncontradoException` e `RegraDeNegocioException`) que o
`GlobalExceptionHandler` traduz pro status HTTP certo (404, 422, 400).

Validação de formato (`@NotBlank`, `@Pattern`, `@Future` etc.) fica nos DTOs
via Bean Validation. Regra de negócio (CPF válido, não pode
cancelar sem observação, etc.) fica dentro da entidade.

## Testes

- `CriarAgendamentoServiceTest`, `AtualizarStatusAgendamentoServiceTest` e
  `BuscarAgendamentoServiceTest` – unitários dos 3 casos de uso, com Mockito
  mockando o repositório.
- `AgendamentoIntegrationTest` – sobe o contexto todo com `@SpringBootTest` e
  testa via `MockMvc`.

```bash
./mvnw test
```

## Diferenciais 

- DTOs em `record`.
- Teste de integração com `@SpringBootTest`.
- `@ConfigurationProperties` de exemplo (prefixo `agendamento`).

## Evidencias

- As evidencias estao na pasta `evidencias` e contem prints do swagger, console do h2 e testes de integração.