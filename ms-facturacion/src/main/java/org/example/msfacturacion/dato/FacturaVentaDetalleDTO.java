package org.example.msfacturacion.dato;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class FacturaVentaDetalleDTO {
    private Long id;
    private Long ventaId;
    private LocalDateTime fechaVenta;
    private BigDecimal totalVenta;
    private List<DetalleVentaDTO> detalles;
}