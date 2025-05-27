package com.example.pdcliente.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id ;
    private  String nombre;
    @Column(unique = true, nullable = false)
    private  String rucDni;
    private  String direccion;
    private  String email;
    private  String telefono;
    private  LocalDateTime  fecha;
    private  String estado;

    public Cliente() {
    }

    public Cliente(Integer id, String nombre, String rucDni, String direccion, String email, String telefono, LocalDateTime fecha, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.rucDni = rucDni;
        this.direccion = direccion;
        this.email = email;
        this.telefono = telefono;
        this.fecha = fecha;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRucDni() {
        return rucDni;
    }

    public void setRucDni(String ruc_dni) {
        this.rucDni = ruc_dni;
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
