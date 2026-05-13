package com.barbershop.barbershop_api.controller;

import com.barbershop.barbershop_api.dto.AppointmentRequestDTO;
import com.barbershop.barbershop_api.entity.Appointment;
import com.barbershop.barbershop_api.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500",
        "https://nicolaspaiva345.github.io"
})
@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    // ✅ Cria um novo agendamento
    // POST /appointments
    @PostMapping
    public ResponseEntity<Appointment> criar(@Valid @RequestBody AppointmentRequestDTO dto) {
        Appointment criado = service.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    // ✅ Lista todos os agendamentos
    // GET /appointments
    @GetMapping
    public ResponseEntity<List<Appointment>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // ✅ Lista agendamentos de um dia específico
    // GET /appointments/dia?data=2025-01-15
    @GetMapping("/dia")
    public ResponseEntity<List<Appointment>> listarPorDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        return ResponseEntity.ok(service.listarPorDia(data));
    }

    // ✅ Lista horários disponíveis de um barbeiro em um dia
    // GET /appointments/disponiveis?data=2025-01-15&barbeiro=João
    @GetMapping("/disponiveis")
    public ResponseEntity<List<LocalTime>> listarDisponiveis(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam String barbeiro) {
        return ResponseEntity.ok(service.listarHorariosDisponiveis(data, barbeiro));
    }

    // ✅ Atualiza um agendamento existente
    // PUT /appointments/1
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    // ✅ Cancela um agendamento (muda status para CANCELADO)
    // PATCH /appointments/1/cancelar
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Appointment> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelar(id));
    }
}