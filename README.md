# ✂️ Barbershop API

API REST para gerenciamento de agendamentos de barbearia, desenvolvida com Java e Spring Boot.

## 🚀 Tecnologias

- Java 21
- Spring Boot 4.0.6
- PostgreSQL (Neon)
- Spring Data JPA / Hibernate
- Spring Validation
- Lombok
- JUnit 5 + Mockito

## 📋 Funcionalidades

- ✅ Criar agendamento
- ✅ Listar todos os agendamentos
- ✅ Listar agendamentos por dia
- ✅ Listar horários disponíveis por barbeiro
- ✅ Atualizar agendamento
- ✅ Cancelar agendamento
- ✅ Validação de conflito de horários
- ✅ Tratamento global de erros

## 🗺️ Endpoints

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/appointments` | Criar agendamento |
| GET | `/appointments` | Listar todos |
| GET | `/appointments/dia?data=YYYY-MM-DD` | Listar por dia |
| GET | `/appointments/disponiveis?data=...&barbeiro=...` | Horários livres |
| PUT | `/appointments/{id}` | Atualizar |
| PATCH | `/appointments/{id}/cancelar` | Cancelar |

## ⚙️ Como rodar localmente

### Pré-requisitos
- Java 21
- PostgreSQL rodando na porta 5432

### Configuração

Crie um arquivo `application-local.properties` em `src/main/resources/`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/barbershop_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

### Executando

```bash
./mvnw spring-boot:run
```

## 🧪 Testes

```bash
./mvnw test
```

## 🌐 Deploy

- **Back-end:** Railway
- **Banco de dados:** Neon (PostgreSQL)
- **Front-end:** GitHub Pages

## 👨‍💻 Autor

Nicolas Paiva — [@nicolaspaiva345](https://github.com/nicolaspaiva345)
