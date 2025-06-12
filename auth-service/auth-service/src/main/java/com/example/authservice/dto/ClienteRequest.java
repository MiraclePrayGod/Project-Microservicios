package com.example.authservice.dto;

import java.time.LocalDateTime;

public class ClienteRequest {

    private String nombre;
    private String rucDni;
    private String direccion;
    private String email;
    private String telefono;
    private LocalDateTime fecha;
    private String estado;

    public ClienteRequest() {}

    public ClienteRequest(String nombre, String rucDni, String direccion, String email, String telefono, LocalDateTime fecha, String estado) {
        this.nombre = nombre;
        this.rucDni = rucDni;
        this.direccion = direccion;
        this.email = email;
        this.telefono = telefono;
        this.fecha = fecha;
        this.estado = estado;
    }

    // Getters y setters

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRucDni() {
        return rucDni;
    }

    public void setRucDni(String rucDni) {
        this.rucDni = rucDni;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
