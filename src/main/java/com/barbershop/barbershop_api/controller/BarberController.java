package com.barbershop.barbershop_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.barbershop.barbershop_api.service.BarberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.barbershop.barbershop_api.dto.barber.CreateBarberRequestDTO;
import jakarta.validation.Valid;
import com.barbershop.barbershop_api.dto.barber.BarberResponseDTO;

@RestController
@RequestMapping("/barbers")
public class BarberController {

    private final BarberService barberService;

    public BarberController(BarberService barberService) {
        this.barberService = barberService;
    }

    @PostMapping
    public BarberResponseDTO create(@Valid @RequestBody CreateBarberRequestDTO request) {

        return barberService.create(request);
    }
}
