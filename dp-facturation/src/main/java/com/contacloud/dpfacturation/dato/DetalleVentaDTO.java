package com.contacloud.dpfacturation.dato;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetalleVentaDTO {
    private Long productoId;
    private int cantidad;
    private BigDecimal precioUnitario;
}