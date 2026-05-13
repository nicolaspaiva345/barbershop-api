package com.barbershop.barbershop_api.service;

import com.barbershop.barbershop_api.dto.AppointmentRequestDTO;
import com.barbershop.barbershop_api.entity.Appointment;
import com.barbershop.barbershop_api.entity.AppointmentStatus;
import com.barbershop.barbershop_api.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    // 💡 @Mock cria um "dublê" do repository
    // Não bate no banco de dados de verdade!
    @Mock
    private AppointmentRepository repository;

    // 💡 @InjectMocks cria o Service passando o dublê acima
    @InjectMocks
    private AppointmentService service;

    // Dados que vamos reutilizar nos testes
    private AppointmentRequestDTO dto;
    private Appointment appointmentExistente;

    @BeforeEach
    void setUp() {
        // ✅ Zeramos segundos e nanosegundos para bater com a grade de horários
        LocalDateTime dataHoraLimpa = LocalDateTime.now()
                .plusDays(1)
                .withHour(9)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        dto = new AppointmentRequestDTO(
                "João Silva",
                "85999998888",
                dataHoraLimpa,
                "Carlos"
        );

        appointmentExistente = new Appointment();
        appointmentExistente.setId(1L);
        appointmentExistente.setNomeCliente("João Silva");
        appointmentExistente.setTelefone("85999998888");
        appointmentExistente.setDataHora(dataHoraLimpa);
        appointmentExistente.setBarbeiro("Carlos");
        appointmentExistente.setStatus(AppointmentStatus.AGENDADO);
    }

    // ===========================
    // TESTES DO MÉTODO salvar()
    // ===========================

    @Test
    @DisplayName("Deve criar agendamento com sucesso quando horário está livre")
    void deveCriarAgendamentoComSucesso() {
        // ARRANGE — prepara o cenário
        // O dublê do repository diz: "não tem conflito nenhum!"
        when(repository.findByDataHoraBetweenAndBarbeiroAndStatus(
                any(), any(), any(), any()
        )).thenReturn(Collections.emptyList());

        // O dublê do repository diz: "salvei e retorno o objeto!"
        when(repository.save(any())).thenReturn(appointmentExistente);

        // ACT — executa a ação que queremos testar
        Appointment resultado = service.salvar(dto);

        // ASSERT — verifica se o resultado é o esperado
        assertNotNull(resultado);
        assertEquals(AppointmentStatus.AGENDADO, resultado.getStatus());
        assertEquals("João Silva", resultado.getNomeCliente());

        // Verifica se o repository.save() foi chamado exatamente 1 vez
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro quando horário já está ocupado")
    void deveLancarErroQuandoHorarioOcupado() {
        // ARRANGE — simula que já tem um agendamento no horário
        when(repository.findByDataHoraBetweenAndBarbeiroAndStatus(
                any(), any(), any(), any()
        )).thenReturn(List.of(appointmentExistente));

        // ACT + ASSERT — verifica se o erro é lançado
        IllegalArgumentException erro = assertThrows(
                IllegalArgumentException.class,
                () -> service.salvar(dto)
        );

        assertEquals("Barbeiro Carlos já tem agendamento neste horário!", erro.getMessage());

        // Verifica que o save() NUNCA foi chamado
        verify(repository, never()).save(any());
    }

    // ===========================
    // TESTES DO MÉTODO cancelar()
    // ===========================

    @Test
    @DisplayName("Deve cancelar agendamento com sucesso")
    void deveCancelarAgendamentoComSucesso() {
        // ARRANGE
        when(repository.findById(1L)).thenReturn(Optional.of(appointmentExistente));
        when(repository.save(any())).thenReturn(appointmentExistente);

        // ACT
        Appointment resultado = service.cancelar(1L);

        // ASSERT
        assertEquals(AppointmentStatus.CANCELADO, resultado.getStatus());
    }

    @Test
    @DisplayName("Deve lançar erro ao cancelar agendamento já cancelado")
    void deveLancarErroAoCancelarAgendamentoJaCancelado() {
        // ARRANGE — simula agendamento já cancelado
        appointmentExistente.setStatus(AppointmentStatus.CANCELADO);
        when(repository.findById(1L)).thenReturn(Optional.of(appointmentExistente));

        // ACT + ASSERT
        IllegalStateException erro = assertThrows(
                IllegalStateException.class,
                () -> service.cancelar(1L)
        );

        assertEquals("Agendamento já está cancelado!", erro.getMessage());
    }

    @Test
    @DisplayName("Deve lançar erro ao cancelar ID inexistente")
    void deveLancarErroAoCancelarIdInexistente() {
        // ARRANGE — simula ID não encontrado
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(
                RuntimeException.class,
                () -> service.cancelar(99L)
        );
    }

    // ===========================
    // TESTES DO listarHorariosDisponiveis()
    // ===========================

    @Test
    @DisplayName("Deve retornar horários disponíveis removendo os ocupados")
    void deveRetornarHorariosDisponiveis() {
        // ARRANGE — simula que 09:00 está ocupado
        when(repository.findByDataHoraBetweenAndBarbeiroAndStatus(
                any(), any(), any(), any()
        )).thenReturn(List.of(appointmentExistente));

        // ACT
        List<LocalTime> disponiveis = service.listarHorariosDisponiveis(
                LocalDate.now().plusDays(1), "Carlos"
        );

        // ASSERT — 09:00 não deve estar na lista!
        assertFalse(disponiveis.contains(LocalTime.of(9, 0)));
        // Mas outros horários devem estar
        assertTrue(disponiveis.contains(LocalTime.of(9, 30)));
    }

    @Test
    @DisplayName("Deve retornar todos os horários quando não há agendamentos")
    void deveRetornarTodosHorariosQuandoNaoHaAgendamentos() {
        // ARRANGE — sem agendamentos no dia
        when(repository.findByDataHoraBetweenAndBarbeiroAndStatus(
                any(), any(), any(), any()
        )).thenReturn(Collections.emptyList());

        // ACT
        List<LocalTime> disponiveis = service.listarHorariosDisponiveis(
                LocalDate.now().plusDays(1), "Carlos"
        );

        // ASSERT — deve ter 18 horários (09:00 até 17:30, de 30 em 30 min)
        assertEquals(18, disponiveis.size());
    }
}