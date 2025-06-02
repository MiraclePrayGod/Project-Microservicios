package contacloud.dpnotificacion.dto;

public class LicenciaDetalleNotificacionDto {
    private String codigoLicencia;
    private String contrasena;

    public LicenciaDetalleNotificacionDto() {
    }

    public LicenciaDetalleNotificacionDto(String codigoLicencia, String contrasena) {
        this.codigoLicencia = codigoLicencia;
        this.contrasena = contrasena;
    }

    public String getCodigoLicencia() {
        return codigoLicencia;
    }

    public void setCodigoLicencia(String codigoLicencia) {
        this.codigoLicencia = codigoLicencia;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
