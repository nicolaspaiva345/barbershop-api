package com.barbershop.barbershop_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Pega o cabeçalho Authorization da requisição
        String authHeader = request.getHeader("Authorization");

        // 2. Se não tem token, deixa passar (rotas públicas)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extrai o token removendo "Bearer "
        String token = authHeader.substring(7);

        // 4. Extrai o email do token
        String email = jwtService.extrairEmail(token);

        // 5. Se o email é válido e não tem autenticação ainda
        if (email != null && SecurityContextHolder.getContext()
                .getAuthentication() == null) {

            // 6. Busca o usuário no banco
            var userDetails = userDetailsService.loadUserByUsername(email);

            // 7. Se o token é válido, autentica o usuário
            if (jwtService.isTokenValido(token)) {
                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 8. Continua para o próximo filtro
        filterChain.doFilter(request, response);
    }
}