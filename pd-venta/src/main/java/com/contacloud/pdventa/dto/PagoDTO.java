package com.contacloud.pdventa.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagoDTO {
    private Long id;
    private Long facturaId;
    private LocalDateTime fechaPago;
    private BigDecimal monto;
    private String metodoPago; // Efectivo, Transferencia, etc.
    private String referencia;
    private Long clienteId;
}
