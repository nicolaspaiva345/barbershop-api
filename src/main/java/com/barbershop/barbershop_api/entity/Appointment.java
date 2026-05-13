
package com.barbershop.barbershop_api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do cliente é obrigatório")
    @Column(nullable = false)
    private String nomeCliente;

    @NotBlank(message = "Telefone é obrigatório")
    @Column(nullable = false)
    private String telefone;

    @NotNull(message = "Data e hora são obrigatórios")
    @Future(message = "O agendamento deve ser para uma data futura")
    @Column(nullable = false)
    private LocalDateTime dataHora;

    @NotBlank(message = "Nome do barbeiro é obrigatório")
    @Column(nullable = false)
    private String barbeiro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status = AppointmentStatus.AGENDADO;
}