package com.barbershop.barbershop_api.dto.barber;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BarberResponseDTO {

    private Long id;

    private String specialty;

    private Long userId;
}
