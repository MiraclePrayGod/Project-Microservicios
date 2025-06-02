package contacloud.dplicencias.dto;

import java.time.LocalDate;

public class VentaDto {
    private int id;
    private String clienteId;
    private LocalDate fecha_emision;
    private String estado;

    public VentaDto() {
    }

    public VentaDto(int id, String clienteId, LocalDate fecha_emision, String estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.fecha_emision = fecha_emision;
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

    public LocalDate getFechaCompra() {
        return fecha_emision;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fecha_emision = fechaCompra;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}