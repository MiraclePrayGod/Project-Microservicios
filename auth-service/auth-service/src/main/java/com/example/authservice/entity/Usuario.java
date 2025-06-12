package com.example.authservice.entity;


import jakarta.persistence.*;
import lombok.Data;




@Data
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String email;
    private String password;
    private String rol; // CLIENTE o ADMINISTRADOR
}

