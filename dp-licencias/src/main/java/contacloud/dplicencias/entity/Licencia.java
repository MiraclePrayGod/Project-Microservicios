package contacloud.dplicencias.entity;


import contacloud.dplicencias.dto.ClienteDto;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Licencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer clienteId;
    private String tipoLicencia;
    private LocalDate fechaActivacion = LocalDate.now();
    private LocalDate fechaExpiracion;
    private String activa;
    @Transient
    private ClienteDto clienteDto;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "licencia_id")  // clave foránea en MatriculaDetalle
    private List<LicenciaDetalle> detalles;

    public Licencia() {
    }

    public Licencia(Integer id, Integer clienteId, String tipoLicencia, LocalDate fechaActivacion,
                    LocalDate fechaExpiracion, String activa, ClienteDto clienteDto,
                    List<LicenciaDetalle> detalles) {
        this.id = id;
        this.clienteId = clienteId;
        this.tipoLicencia = tipoLicencia;
        this.fechaActivacion = fechaActivacion;
        this.fechaExpiracion = fechaExpiracion;
        this.activa = activa;
        this.clienteDto = clienteDto;
        this.detalles = detalles;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDate getFechaActivacion() {
        return fechaActivacion;
    }

    public void setFechaActivacion(LocalDate fechaActivacion) {
        this.fechaActivacion = fechaActivacion;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDate fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getActiva() {
        return activa;
    }

    public void setActiva(String activa) {
        this.activa = activa;
    }

    public ClienteDto getClienteDto() {
        return clienteDto;
    }

    public void setClienteDto(ClienteDto clienteDto) {
        this.clienteDto = clienteDto;
    }

    public List<LicenciaDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<LicenciaDetalle> detalles) {
        this.detalles = detalles;
    }
}
