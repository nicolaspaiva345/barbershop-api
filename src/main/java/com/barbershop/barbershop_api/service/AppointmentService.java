package com.barbershop.barbershop_api.service;

import com.barbershop.barbershop_api.dto.AppointmentRequestDTO;
import com.barbershop.barbershop_api.entity.Appointment;
import com.barbershop.barbershop_api.entity.AppointmentStatus;
import com.barbershop.barbershop_api.repository.AppointmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    // 💡 É como abrir o diário — cada classe tem o seu!
    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    private final AppointmentRepository repository;

    public AppointmentService(AppointmentRepository repository) {
        this.repository = repository;
    }

    public List<Appointment> listarTodos() {
        log.info("Listando todos os agendamentos");
        return repository.findAll();
    }

    public List<Appointment> listarPorDia(LocalDate data) {
        log.info("Listando agendamentos do dia: {}", data);
        return repository.findByDataHoraBetween(
                data.atStartOfDay(),
                data.atTime(23, 59, 59)
        );
    }

    public Appointment salvar(AppointmentRequestDTO dto) {
        log.info("Tentando criar agendamento para {} com barbeiro {} às {}",
                dto.nomeCliente(), dto.barbeiro(), dto.dataHora());

        verificarConflitoDeHorario(dto.dataHora(), dto.barbeiro(), null);

        Appointment appointment = new Appointment();
        appointment.setNomeCliente(dto.nomeCliente());
        appointment.setTelefone(dto.telefone());
        appointment.setDataHora(dto.dataHora());
        appointment.setBarbeiro(dto.barbeiro());
        appointment.setStatus(AppointmentStatus.AGENDADO);

        Appointment salvo = repository.save(appointment);
        log.info("Agendamento criado com sucesso! ID: {}", salvo.getId());
        return salvo;
    }

    public Appointment atualizar(Long id, AppointmentRequestDTO dto) {
        log.info("Atualizando agendamento ID: {}", id);

        Appointment existente = buscarPorIdOuLancarErro(id);
        verificarConflitoDeHorario(dto.dataHora(), dto.barbeiro(), id);

        existente.setNomeCliente(dto.nomeCliente());
        existente.setTelefone(dto.telefone());
        existente.setDataHora(dto.dataHora());
        existente.setBarbeiro(dto.barbeiro());

        Appointment atualizado = repository.save(existente);
        log.info("Agendamento ID: {} atualizado com sucesso!", id);
        return atualizado;
    }

    public Appointment cancelar(Long id) {
        log.info("Tentando cancelar agendamento ID: {}", id);

        Appointment existente = buscarPorIdOuLancarErro(id);

        if (existente.getStatus() == AppointmentStatus.CANCELADO) {
            log.warn("Tentativa de cancelar agendamento já cancelado! ID: {}", id);
            throw new IllegalStateException("Agendamento já está cancelado!");
        }

        existente.setStatus(AppointmentStatus.CANCELADO);
        Appointment cancelado = repository.save(existente);
        log.info("Agendamento ID: {} cancelado com sucesso!", id);
        return cancelado;
    }

    public List<LocalTime> listarHorariosDisponiveis(LocalDate data, String barbeiro) {
        log.info("Buscando horários disponíveis para barbeiro {} no dia {}", barbeiro, data);

        List<LocalTime> todosOsHorarios = gerarGradeHorarios();

        List<Appointment> agendados = repository.findByDataHoraBetweenAndBarbeiroAndStatus(
                data.atStartOfDay(),
                data.atTime(23, 59, 59),
                barbeiro,
                AppointmentStatus.AGENDADO
        );

        Set<LocalTime> ocupados = agendados.stream()
                .map(a -> a.getDataHora().toLocalTime())
                .collect(Collectors.toSet());

        List<LocalTime> disponiveis = todosOsHorarios.stream()
                .filter(horario -> !ocupados.contains(horario))
                .toList();

        log.info("Encontrados {} horários disponíveis para {} no dia {}",
                disponiveis.size(), barbeiro, data);
        return disponiveis;
    }

    // ──────────────────────────────────────────
    // MÉTODOS PRIVADOS
    // ──────────────────────────────────────────

    private List<LocalTime> gerarGradeHorarios() {
        List<LocalTime> horarios = new ArrayList<>();
        LocalTime hora = LocalTime.of(9, 0);
        LocalTime limite = LocalTime.of(18, 0);

        while (hora.isBefore(limite)) {
            horarios.add(hora);
            hora = hora.plusMinutes(30);
        }
        return horarios;
    }

    private Appointment buscarPorIdOuLancarErro(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Agendamento com ID {} não encontrado!", id);
                    return new RuntimeException(
                            "Agendamento com ID " + id + " não encontrado!"
                    );
                });
    }

    private void verificarConflitoDeHorario(LocalDateTime dataHora,
                                            String barbeiro,
                                            Long idIgnorar) {
        List<Appointment> conflitos = repository.findByDataHoraBetweenAndBarbeiroAndStatus(
                dataHora.minusMinutes(1),
                dataHora.plusMinutes(1),
                barbeiro,
                AppointmentStatus.AGENDADO
        );

        boolean temConflito = conflitos.stream()
                .anyMatch(a -> !a.getId().equals(idIgnorar));

        if (temConflito) {
            log.warn("Conflito de horário detectado! Barbeiro: {} às {}", barbeiro, dataHora);
            throw new IllegalArgumentException(
                    "Barbeiro " + barbeiro + " já tem agendamento neste horário!"
            );
        }
    }
}