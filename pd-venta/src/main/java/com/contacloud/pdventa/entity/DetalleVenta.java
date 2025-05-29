package com.contacloud.pdventa.entity;

import com.contacloud.pdventa.dto.ClienteDTO;
import com.contacloud.pdventa.dto.ProductoDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productoId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    @Transient
    private ProductoDTO productoDTO;
    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;
}
