package contacloud.dplicencias.dto;

public class LicenciaDetalleCreateDto {
    private Integer ventaId;

    public LicenciaDetalleCreateDto() {
    }

    public LicenciaDetalleCreateDto(Integer ventaId) {
        this.ventaId = ventaId;
    }

    public Integer getVentaId() {
        return ventaId;
    }

    public void setVentaId(Integer ventaId) {
        this.ventaId = ventaId;
    }
}
