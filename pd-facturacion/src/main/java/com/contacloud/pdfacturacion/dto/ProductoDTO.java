package com.contacloud.pdfacturacion.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String categoria;
    private BigDecimal precioUnitario;
    private Integer stock;
    private Integer stockMinimo;
    private String unidadMedida;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
