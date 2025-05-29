package contacloud.dplicencias.feign;

import contacloud.dplicencias.dto.ClienteDto;
import contacloud.dplicencias.dto.VentaDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "pd-venta", path = "/ventas")
public interface VentaFeing {
        @GetMapping("/{id}")
        ResponseEntity<VentaDto> obtenerPorId(@PathVariable Integer id);

        @PutMapping("/{id}")
        ResponseEntity<VentaDto> actualizarVenta(@PathVariable Integer id, @RequestBody ClienteDto cursoDto);

}
