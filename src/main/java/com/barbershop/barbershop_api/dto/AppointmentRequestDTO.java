package com.barbershop.barbershop_api.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record AppointmentRequestDTO(

        @NotBlank(message = "Nome do cliente é obrigatório")
        @Size(max = 100, message = "Nome muito grande")
        String nomeCliente,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(
                regexp = "^\\(?\\d{2}\\)?\\s?9?\\d{4}-?\\d{4}$",
                message = "Telefone inválido"
        )
        String telefone,

        @NotNull(message = "Data e hora são obrigatórios")
        @Future(message = "O agendamento deve ser para uma data futura")
        LocalDateTime dataHora,

        @NotBlank(message = "Nome do barbeiro é obrigatório")
        String barbeiro

) {}