package com.barbershop.barbershop_api.service;

import com.barbershop.barbershop_api.dto.LoginRequestDTO;
import com.barbershop.barbershop_api.dto.LoginResponseDTO;
import com.barbershop.barbershop_api.entity.User;
import com.barbershop.barbershop_api.repository.UserRepository;
import com.barbershop.barbershop_api.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // ✅ Cadastra um novo barbeiro
    public LoginResponseDTO cadastrar(User user) {
        log.info("Tentando cadastrar usuário: {}", user.getEmail());

        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("Email já cadastrado: {}", user.getEmail());
            throw new IllegalArgumentException("Email já cadastrado!");
        }

        // 💡 Nunca salva a senha pura — sempre criptografada!
        user.setSenha(passwordEncoder.encode(user.getSenha()));

        userRepository.save(user);
        log.info("Usuário cadastrado com sucesso: {}", user.getEmail());

        String token = jwtService.gerarToken(user.getEmail());
        return new LoginResponseDTO(
                token,
                user.getNome(),
                user.getEmail(),
                user.getRole().name()
        );
    }

    // ✅ Faz o login e retorna o token
    public LoginResponseDTO login(LoginRequestDTO dto) {
        log.info("Tentativa de login: {}", dto.email());

        // 💡 O AuthenticationManager verifica email e senha
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email(),
                        dto.senha()
                )
        );

        // Se chegou aqui, login foi bem sucedido!
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado!"));

        String token = jwtService.gerarToken(user.getEmail());
        log.info("Login bem sucedido: {}", dto.email());

        return new LoginResponseDTO(
                token,
                user.getNome(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}