package org.example.msfacturacion.dato;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String unidad;
    private BigDecimal precio;
    private Integer stock;
}