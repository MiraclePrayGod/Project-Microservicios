package contacloud.dplicencias.dto;

import java.time.LocalDate;

public class VentaDto {
    private int id;
    private String clienteId;
    private LocalDate fechaEmision;
    private String estado;

    public VentaDto() {
    }

    public VentaDto(int id, String clienteId, LocalDate fechaEmision, String estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.fechaEmision = fechaEmision;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDate getFechaEemision() {
        return fechaEmision;
    }

    public void setFechaEmision(LocalDate fechaCompra) {
        this.fechaEmision = fechaCompra;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}