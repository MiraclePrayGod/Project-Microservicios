package contacloud.dpnotificacion.entity;


import contacloud.dpnotificacion.dto.ClienteDto;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Notificacion {




    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificacionId;
    private Integer clienteId;
    private String tipo; // email, SMS, push
    private String asunto;
    @Lob
    private String mensaje;
    private String estado; // pendiente, enviado, fallido
    private LocalDateTime fechaEnvio;
    @Lob
    private String respuesta;
    @Transient
    private ClienteDto clienteDto;

    public Notificacion() {
    }

    public Notificacion(Integer notificacionId, Integer clienteId, String tipo,
                         String asunto, String mensaje, String estado,
                        LocalDateTime fechaEnvio, String respuesta,ClienteDto clienteDto) {
        this.notificacionId = notificacionId;
        this.clienteId = clienteId;
        this.tipo = tipo;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.estado = estado;
        this.fechaEnvio = fechaEnvio;
        this.respuesta = respuesta;
        this.clienteDto = clienteDto;
    }

    public Integer getNotificacionId() {
        return notificacionId;
    }

    public void setNotificacionId(Integer notificacionId) {
        this.notificacionId = notificacionId;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public ClienteDto getClienteDto() {
        return clienteDto;
    }

    public void setClienteDto(ClienteDto clienteDto) {
        this.clienteDto = clienteDto;
    }


}
