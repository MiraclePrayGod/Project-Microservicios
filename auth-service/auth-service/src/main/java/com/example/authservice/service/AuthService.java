package com.example.authservice.service;

import com.example.authservice.config.JwtUtil;
import com.example.authservice.dto.AuthRequest;
import com.example.authservice.dto.AuthResponse;
import com.example.authservice.dto.ClienteRequest;
import com.example.authservice.dto.RegisterRequest;
import com.example.authservice.entity.Usuario;
import com.example.authservice.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    public AuthResponse register(RegisterRequest request) {
        // Crear usuario
        Usuario user = new Usuario();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRol(request.getRol());

        repository.save(user);

        // Registrar cliente si rol es CLIENTE

        if (request.getRol().equalsIgnoreCase("CLIENTE")) {
            ClienteRequest cliente = new ClienteRequest();
            cliente.setNombre(request.getNombre());
            cliente.setRucDni(request.getRucDni());
            cliente.setEmail(request.getEmail());
            cliente.setTelefono(request.getTelefono());
            cliente.setDireccion(request.getDireccion());
            cliente.setEstado("ACTIVO");
            cliente.setFecha(LocalDateTime.now());

            restTemplate.postForEntity("http://pd-cliente/clientes", cliente, ClienteRequest.class);
        }



        // Retornar token JWT
//        return new AuthResponse(jwtUtil.generateToken(user.getUsername(), user.getRol()));
        return new AuthResponse(
                jwtUtil.generateToken(user.getUsername(), user.getRol()),
                user.getRol(),
                user.getEmail()
        );

    }

    public AuthResponse login(AuthRequest request) {
        Usuario user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        return new AuthResponse(
                jwtUtil.generateToken(user.getUsername(), user.getRol()),
                user.getRol(),
                user.getEmail());

    }
}
