package com.contacloud.pdfacturacion.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaPago;
    private BigDecimal monto;
    private String metodoPago;
    private String referencia;

    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;
}
