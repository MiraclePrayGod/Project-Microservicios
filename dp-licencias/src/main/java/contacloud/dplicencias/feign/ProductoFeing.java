package contacloud.dplicencias.feign;


import contacloud.dplicencias.dto.ProductoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "dp-inventario", path = "/productos")
public interface ProductoFeing {

//    @GetMapping("/{id}")
//    ResponseEntity<ProductoDto> obtenerPorId(@PathVariable Long id);
    @GetMapping("/{id}")
    ProductoDto obtenerProductoPorId(@PathVariable Long id);3
}
