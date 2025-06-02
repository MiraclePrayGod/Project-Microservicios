package contacloud.dplicencias.dto;

import java.time.LocalDate;
import java.util.List;

public class LicenciaCreateDto {
    private Integer clienteId;
    private String tipoLicencia;
    private LocalDate fechaExpiracion;
    private Boolean estado;
    private List<LicenciaDetalleCreateDto> detalles;

    public LicenciaCreateDto() {

    }

    public LicenciaCreateDto(Integer clienteId, String tipoLicencia,
                             LocalDate fechaExpiracion,
                             Boolean estado,
                             List<LicenciaDetalleCreateDto> detalles) {
        this.clienteId = clienteId;
        this.tipoLicencia = tipoLicencia;
        this.fechaExpiracion = fechaExpiracion;
        this.estado = estado;
        this.detalles = detalles;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public String getTipoLicencia() {
        return tipoLicencia;
    }

    public void setTipoLicencia(String tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDate fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public List<LicenciaDetalleCreateDto> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<LicenciaDetalleCreateDto> detalles) {
        this.detalles = detalles;
    }
}