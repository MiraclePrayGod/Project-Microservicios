package contacloud.dplicencias.dto;

import java.time.LocalDate;

public class VentaDto {
    private int id;
    private String clienteId;
    private String producto;
    private Double precio;
    private LocalDate fechaCompra;
    private String estado;

    public VentaDto() {
    }

    public VentaDto(int id, String clienteId, String producto,
                    Double precio, LocalDate fechaCompra, String estado) {
        this.id = id;
        this.clienteId = clienteId;
        this.producto = producto;
        this.precio = precio;
        this.fechaCompra = fechaCompra;
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

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}