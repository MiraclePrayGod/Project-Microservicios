package com.contacloud.pdfacturacion.dto;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class DetalleFacturaDTO {
    private Long productoId;
    private Integer cantidad;
    private BigDecimal precioUnitario; // Puede venir del servicio de productos
}
