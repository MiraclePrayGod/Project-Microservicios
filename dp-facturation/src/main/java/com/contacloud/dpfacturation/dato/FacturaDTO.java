package com.contacloud.dpfacturation.dato;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class FacturaDTO {
    private Long id;
    private Long clienteId;
    private LocalDateTime fechaEmision;
    private String numero;
    private BigDecimal total;
    private String estado;
    private List<FacturaVentaDetalleDTO> ventas;
}