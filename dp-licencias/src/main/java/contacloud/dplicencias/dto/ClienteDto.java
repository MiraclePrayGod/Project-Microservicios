package contacloud.dplicencias.dto;

public class ClienteDto {
    private Integer id;      // opcional, para identificar al cliente
    private String nombre;
    private String email;
    private String rucDni;

    public ClienteDto() {}

    public ClienteDto(Integer id, String nombre, String direccion, String rucDni) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rucDni = rucDni;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRucDni() { return rucDni; }
    public void setRucDni(String rucDni) { this.rucDni = rucDni; }
}