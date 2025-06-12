package com.example.authservice.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String rol;

    // Campos adicionales para CLIENTE
    private String nombre;
    private String rucDni;
    private String telefono;
    private String direccion;
}

