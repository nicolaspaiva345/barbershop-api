package com.barbershop.barbershop_api.dto.barber;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBarberRequestDTO {

    @NotNull
    private Long userId;

    @NotBlank
    private String specialty;
}
