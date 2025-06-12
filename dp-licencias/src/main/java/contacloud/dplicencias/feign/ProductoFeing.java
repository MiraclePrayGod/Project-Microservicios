package contacloud.dplicencias.feign;

import contacloud.dplicencias.dto.ClienteDto;
import contacloud.dplicencias.dto.ProductoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "dp-inventario", path = "/productos")
public interface ProductoFeing {
        @GetMapping("/{id}")
        ResponseEntity<ProductoDto> obtenerPorId(@PathVariable Integer id);

        @PutMapping("/{id}")
        ResponseEntity<ProductoDto> actualizarCliente(@PathVariable Integer id, @RequestBody ClienteDto cursoDto);

}
