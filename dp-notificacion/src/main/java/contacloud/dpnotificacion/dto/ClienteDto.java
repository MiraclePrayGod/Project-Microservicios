package contacloud.dpnotificacion.dto;

public class ClienteDto {
    private Integer id;      // opcional, para identificar al cliente
    private String email;
    private String estado;
    // campo que realmente necesitas

    public ClienteDto() {}

    public ClienteDto(Integer id, String email,String estado) {
        this.id = id;
        this.email = email;
        this.estado = estado;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}