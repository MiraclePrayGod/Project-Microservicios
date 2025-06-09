package com.contacloud.pdventa.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetalleVentaDTO {
    private Long productoId;
    private Integer cantidad;
    private BigDecimal precioUnitario;

    // Campos adicionales del producto
    private String nombreProducto;
    private String categoria;
    private String unidadMedida;
}
