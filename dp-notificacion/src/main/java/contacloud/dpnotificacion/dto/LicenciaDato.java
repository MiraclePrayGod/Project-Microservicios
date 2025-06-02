package contacloud.dpnotificacion.dto;

import java.util.List;

public class LicenciaDato {
    private Boolean estado;
    private List<LicenciaDetalleNotificacionDto> detalles;

    public LicenciaDato() {
    }

    public LicenciaDato(Boolean estado, List<LicenciaDetalleNotificacionDto> detalles) {
        this.estado = estado;
        this.detalles = detalles;
    }

    public List<LicenciaDetalleNotificacionDto> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<LicenciaDetalleNotificacionDto> detalles) {
        this.detalles = detalles;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
