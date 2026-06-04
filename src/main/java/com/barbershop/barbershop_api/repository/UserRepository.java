package com.barbershop.barbershop_api.repository;

import com.barbershop.barbershop_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Busca usuário pelo email — usado no login
    Optional<User> findByEmail(String email);

    // Verifica se já existe um usuário com esse email
    boolean existsByEmail(String email);
}