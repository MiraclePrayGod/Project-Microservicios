package com.contacloud.dpfacturation.dato;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VentaDTO {
    private Long id;
    private Long clienteId;
    private LocalDateTime fechaEmision;
    private BigDecimal total;
    private String estado;                  // PENDIENTE | PAGADO
    private List<DetalleVentaDTO> detalles;
}