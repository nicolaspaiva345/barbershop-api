package com.barbershop.barbershop_api.repository;

import com.barbershop.barbershop_api.entity.Appointment;
import com.barbershop.barbershop_api.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Busca agendamento por data/hora exata
    Optional<Appointment> findByDataHora(LocalDateTime dataHora);

    // Busca agendamentos entre dois horários
    List<Appointment> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    // ✅ NOVO: Busca por intervalo de horário + barbeiro + status
    // Usado para verificar conflitos e listar horários disponíveis
    List<Appointment> findByDataHoraBetweenAndBarbeiroAndStatus(
            LocalDateTime inicio,
            LocalDateTime fim,
            String barbeiro,
            AppointmentStatus status
    );
}