package com.contacloud.pdventa.entity;


import com.contacloud.pdventa.dto.ClienteDTO;
import com.contacloud.pdventa.dto.ProductoDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clienteId;
    private LocalDateTime fechaEmision;
    private BigDecimal total;
    private String estado;
    @Transient
    private ClienteDTO clienteDTO;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<DetalleVenta> detalles;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private List<Pago> pagos;


}
