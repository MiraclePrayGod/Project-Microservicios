package org.example.msfacturacion.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "factura")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clienteId;
    private LocalDateTime fechaEmision;
    private String numero;            // Serie‑Correlativo
    private BigDecimal total;
    private String estado;            // GENERADA | ANULADA

    /**
     * Cargamos ventas en modo EAGER para evitar LazyInitializationException al serializar a JSON.
     * Como cada Factura sólo contiene la lista de IDs y montos de ventas, el impacto de carga es bajo.
     */
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<FacturaVenta> ventas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<FacturaVenta> getVentas() {
        return ventas;
    }

    public void setVentas(List<FacturaVenta> ventas) {
        this.ventas = ventas;
    }
}