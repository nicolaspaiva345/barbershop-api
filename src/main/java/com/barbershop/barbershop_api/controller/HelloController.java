package com.barbershop.barbershop_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Servidor da barbearia funcionando!";
    }
    @GetMapping("/status")
    public  String status() {
        return "API Online";
    }
}