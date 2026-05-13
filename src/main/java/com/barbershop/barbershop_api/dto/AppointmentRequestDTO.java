package com.barbershop.barbershop_api.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AppointmentRequestDTO(

        @NotBlank(message = "Nome do cliente é obrigatório")
        String nomeCliente,

        @NotBlank(message = "Telefone é obrigatório")
        String telefone,

        @NotNull(message = "Data e hora são obrigatórios")
        @Future(message = "O agendamento deve ser para uma data futura")
        LocalDateTime dataHora,

        @NotBlank(message = "Nome do barbeiro é obrigatório")
        String barbeiro
) {}