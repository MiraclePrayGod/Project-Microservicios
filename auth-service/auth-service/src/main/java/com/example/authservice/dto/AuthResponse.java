package com.example.authservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private String rol;
    private String email;

    public AuthResponse(String token, String rol, String email) {
        this.token = token;
        this.rol = rol;
        this.email = email;
    }
}

