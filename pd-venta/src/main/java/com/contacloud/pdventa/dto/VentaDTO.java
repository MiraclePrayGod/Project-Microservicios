package com.contacloud.pdventa.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VentaDTO {
    private Long id;
    private Integer clienteId;
    private LocalDateTime fechaEmision;
    private BigDecimal total;
    private String estado; // PENDIENTE, PAGADA, ANULADA
    private List<DetalleVentaDTO> detalles; // Detalles de la factura
}
