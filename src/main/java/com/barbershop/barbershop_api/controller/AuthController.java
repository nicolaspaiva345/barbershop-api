package com.barbershop.barbershop_api.controller;

import com.barbershop.barbershop_api.dto.LoginRequestDTO;
import com.barbershop.barbershop_api.dto.LoginResponseDTO;
import com.barbershop.barbershop_api.entity.User;
import com.barbershop.barbershop_api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {
        "http://localhost:5500",
        "http://127.0.0.1:5500",
        "https://nicolaspaiva345.github.io"
})
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ✅ Cadastrar novo barbeiro
    // POST /auth/cadastrar
    @PostMapping("/cadastrar")
    public ResponseEntity<LoginResponseDTO> cadastrar(
            @Valid @RequestBody User user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.cadastrar(user));
    }

    // ✅ Login
    // POST /auth/login
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }
}