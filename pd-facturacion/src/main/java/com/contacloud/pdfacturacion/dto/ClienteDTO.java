package com.contacloud.pdfacturacion.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClienteDTO {
    private Long id;
    private String nombre;
    private String rucDni;
    private String direccion;
    private String email;
    private String telefono;
    private LocalDateTime fecha;
    private String estado;
}
