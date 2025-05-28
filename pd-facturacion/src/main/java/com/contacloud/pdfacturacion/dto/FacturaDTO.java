package com.contacloud.pdfacturacion.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FacturaDTO {
    private Long id;
    private Long clienteId;
    private LocalDateTime fechaEmision;
    private BigDecimal total;
    private String estado; // PENDIENTE, PAGADA, ANULADA
    private List<DetalleFacturaDTO> detalles; // Detalles de la factura
}
