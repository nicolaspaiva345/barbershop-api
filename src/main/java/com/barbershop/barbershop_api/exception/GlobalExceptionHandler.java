package com.barbershop.barbershop_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ Trata erros de validação (@NotBlank, @Future, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarErrosDeValidacao(
            MethodArgumentNotValidException ex) {

        // Coleta todos os campos com erro e suas mensagens
        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", 400);
        resposta.put("erro", "Dados inválidos");
        resposta.put("mensagens", erros);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    // ✅ Trata erros de regra de negócio (ex: horário ocupado)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> tratarErroDeNegocio(
            IllegalArgumentException ex) {

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", 400);
        resposta.put("erro", "Regra de negócio violada");
        resposta.put("mensagem", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    // ✅ Trata erros de estado inválido (ex: cancelar algo já cancelado)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> tratarErroDeEstado(
            IllegalStateException ex) {

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", 400);
        resposta.put("erro", "Operação inválida");
        resposta.put("mensagem", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    // ✅ Trata recurso não encontrado (ex: ID que não existe)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> tratarNaoEncontrado(
            RuntimeException ex) {

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", 404);
        resposta.put("erro", "Recurso não encontrado");
        resposta.put("mensagem", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    }
}