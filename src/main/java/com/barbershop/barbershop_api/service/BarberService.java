package com.barbershop.barbershop_api.service;

import com.barbershop.barbershop_api.dto.barber.BarberResponseDTO;
import org.springframework.stereotype.Service;
import com.barbershop.barbershop_api.repository.BarberRepository;
import com.barbershop.barbershop_api.entity.Barber;
import com.barbershop.barbershop_api.repository.UserRepository;
import com.barbershop.barbershop_api.dto.barber.CreateBarberRequestDTO;
import com.barbershop.barbershop_api.entity.User;
import java.util.Optional;

@Service
public class BarberService {

    private final BarberRepository barberRepository;
    private final UserRepository userRepository;

    public BarberService(
            BarberRepository barberRepository,
            UserRepository userRepository
    ) {
        this.barberRepository = barberRepository;
        this.userRepository = userRepository;
    }

    public BarberResponseDTO create(CreateBarberRequestDTO request) {

        Optional<User> userOptional =
                userRepository.findById(request.getUserId());

        User user = userOptional.orElseThrow(
                () -> new RuntimeException("User not found")
        );

        boolean barberAlreadyExists =
                barberRepository.existsByUserId(user.getId());

        if (barberAlreadyExists) {
            throw new RuntimeException("User is already a barber");
        }

        Barber barber = new Barber(
                user,
                request.getSpecialty()
        );

        Barber savedBarber = barberRepository.save(barber);

        return new BarberResponseDTO(
                savedBarber.getId(),
                savedBarber.getSpecialty(),
                savedBarber.getUser().getId()
        );
    }
}