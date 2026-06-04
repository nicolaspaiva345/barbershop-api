package com.barbershop.barbershop_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // 💡 Chave secreta para assinar o token
    // Fica no application.properties — nunca no código!
    @Value("${jwt.secret}")
    private String secret;

    // Tempo de expiração: 24 horas
    private static final long EXPIRATION = 1000 * 60 * 60 * 24;

    // ✅ Gera um token JWT para o usuário
    public String gerarToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSecretKey())
                .compact();
    }

    // ✅ Extrai o email do token
    public String extrairEmail(String token) {
        return getClaims(token).getSubject();
    }

    // ✅ Verifica se o token é válido
    public boolean isTokenValido(String token) {
        try {
            return getClaims(token)
                    .getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // ──────────────────────────────
    // MÉTODOS PRIVADOS
    // ──────────────────────────────

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}