package com.barbershop.barbershop_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.barbershop.barbershop_api.entity.Barber;

public interface BarberRepository extends JpaRepository<Barber, Long> {

    boolean existsByUserId(Long userId);

}