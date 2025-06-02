package contacloud.dplicencias.dto;

public class ProductoDto {
    private String nombre;


    public ProductoDto() {
    }

    public ProductoDto(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
