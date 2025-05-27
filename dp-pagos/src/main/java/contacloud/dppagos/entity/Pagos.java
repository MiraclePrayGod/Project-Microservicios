package contacloud.dppagos.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
public class Pagos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pagoId;
    private Integer facturaId;
    private LocalDateTime fechaPago;
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    private BigDecimal monto;
    @Pattern(regexp = "tarjeta|transferencia|efectivo|otro", message = "Método de pago inválido")
    private String metodoPago;
    @Pattern(regexp = "pendiente|completado|rechazado", message = "Estado inválido")
    private String estado;
    @Column(unique = true, nullable = false)
    private String referencia;
    private String observaciones;

//regex signigica expresion regular


    public Pagos() {
    }

    public Pagos(Integer pagoId, Integer facturaId, LocalDateTime fechaPago, BigDecimal monto, String metodoPago, String estado, String referencia, String observaciones) {
        this.pagoId = pagoId;
        this.facturaId = facturaId;
        this.fechaPago = fechaPago;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.estado = estado;
        this.referencia = referencia;
        this.observaciones = observaciones;
    }

    public Integer getPagoId() {
        return pagoId;
    }

    public void setPagoId(Integer pagoId) {
        this.pagoId = pagoId;
    }

    public Integer getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(Integer facturaId) {
        this.facturaId = facturaId;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
